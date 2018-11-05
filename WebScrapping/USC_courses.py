import json

from bs4 import BeautifulSoup
from datetime import datetime, timedelta
import urllib.request
import ssl

# only focus on undergraduate
CLASS_LEVEL_LIMIT = 500
DAY_ABBREVIATIONS = ["Mon", "Tue", "Wed", "Thu", "Fri"]
DAY_NAMES = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]


def get_course_number(course_description):
    pos = course_description.find(":")
    result = []
    for letter in course_description[:pos]:
        if (letter.isdigit()):
            result.append(letter)
    return int("".join(result))


def get_course_department(course_description):
    return course_description.split(" ")[0]


def get_course_units(course_description):
    '''
    :param course_description: the title of the course on USC website
    :return: a string that only includes the least amount of units
    '''
    return course_description[course_description.find("(") + 1:course_description.rfind(" ")]


def get_section_time(time_text):
    '''

    :param time_text: a string in this format "10:00-10:50am"
    :return: a tuple of start time and end time in 24 hour format
    '''
    # print(time_text)
    if time_text.find("-") == -1:
        return (None, None)
    start_time_str, end_time_str = time_text.split("-")[:2]
    suffix = time_text[-2:]

    start_time = datetime.strptime(start_time_str + " " + suffix, "%I:%M %p")
    start_time.strftime("%H:%M")
    end_time = datetime.strptime(end_time_str, "%I:%M%p")
    end_time.strftime("%H:%M")
    # adjust for 11:00-12:00pm situation
    if end_time < start_time:
        start_time -= timedelta(hours=12)
    # print("Time is:",start_time.time(),end_time.time())
    return (start_time.time(), end_time.time())


def get_section_number(section_number):
    '''

    :param section_number: section number and a letter D or R
    :return: section number
    '''
    return section_number[:-1]


def get_section_day(day_text):
    '''

    :param day_text: day description on classes.usc
    :return: a list containing the day of the section in number, such as [1,2] means Mon and Tue
    '''
    days = day_text.strip().split(",")
    for index in range(0, len(days)):
        days[index] = days[index].strip()
    day = []
    # if there is one word
    if (len(days) == 1):
        if len(days[0]) == 3:
            if days[0] == "TBA":
                return [-1]
            elif days[0] == "MWF":
                return [1, 3, 5]
            else:
                print("days")
        # section only has 1 day
        else:
            for index in range(0, len(DAY_NAMES)):
                if days[0] == DAY_NAMES[index]:
                    day.append(index + 1)
            return day
    # if there are 2 words
    if len(days) == 2:
        for i in range(0, len(days)):
            for index in range(0, len(DAY_ABBREVIATIONS)):
                if days[i] == DAY_ABBREVIATIONS[index]:
                    day.append(index + 1)
                    break
        return day
    return day


def get_section_info(index, sectionID_list, type_list, time_list, day_list):
    '''

    :param index:
    :param sectionID_list: section ID in one course
    :param type_list: types in one course
    :param time_list: times for one course
    :param day_list:  days for one course
    :return: a section with its info
    '''
    section_info = dict()
    section_info["SectionID"] = get_section_number(sectionID_list[index].text)
    section_info["Type"] = type_list[index].text
    section_info["StartTime"] = str(get_section_time(time_list[index].text)[0])
    section_info["EndTime"] = str(get_section_time(time_list[index].text)[1])
    section_info["Days"] = get_section_day(day_list[index].text)
    return section_info


def check_section_info_integrity(section_info):
    '''

    :param section_info: a section_info dictionary
    :return: a bool decide whether section_info can be
    '''
    # check time not empty
    if section_info["StartTime"] is None or section_info["EndTime"] is None:
        return False
    # check days not TBA
    if -1 in section_info["Days"]:
        return False
    return True


def get_units_list(soup):
    '''
    get the range of the units
    :param soup: beautiful soup object
    :return:
    '''
    unitsList = []
    for course in soup.find_all('div', class_='course-id'):
        for units in course.find_all('span', class_='units'):
            if (get_course_units(units.text) not in unitsList):
                unitsList.append(get_course_units(units.text))
    unitsList.sort()
    return unitsList


def get_num_students_in_class(registered):
    pos = registered.find("of")
    if (pos < 0):
        return 0
    return int(registered[:pos])


def get_department_name(soup):
    '''

    :param soup: python soup object
    :return: department name
    '''
    return soup.find("abbr").text


def get_students_experience(registeredList):
    '''

    :param registeredList: students in each class
    :return: expected experience
    '''
    temp = []
    sumStudents = int()
    sumExperience = int()
    # calculate the experience, which is number of students ** 2
    for students in registeredList:
        temp.append(students ** 2)
    # get all number students in classes
    for num in registeredList:
        sumStudents += num
    # get the student experience
    for num in temp:
        sumExperience += num
    # avoid problem of zero
    if (sumStudents == 0):
        return 0
    else:
        return sumExperience / sumStudents


def get_department_courses_list(url):
    '''
    get one department's courses, containing each course
    each course has several sections
    each section has sectionID, type, startTime, endTime, days
    :param url:course url on classes.usc
    :return: a list containing information above
    '''
    new_context = ssl._create_unverified_context()
    page = urllib.request.urlopen(url, context=new_context)
    soup = BeautifulSoup(page.read(), "html.parser")

    course_list = list()
    department_dict = dict()
    # get department name
    department_name = get_department_name(soup)

    # go through each course
    for course in soup.find_all('div', class_="course-info"):
        # course_description example: CSCI 104L: Data Structures and Object Oriented Design (4.0 units)
        course_description = course.find('a', class_='courselink').text
        print()
        # print(course_description)

        print(get_course_department(course_description),
              get_course_number(course_description),
              get_course_units(course_description))

        # get all sections under one course
        section_list = course.find('table', class_="sections responsive")

        sectionID_list = course.find_all('td', class_="section")
        time_list = section_list.find_all('td', class_="time")
        type_list = section_list.find_all('td', class_="type")
        day_list = section_list.find_all('td', class_="days")

        # sections under one course
        course_sections = list()

        for idx in range(len(type_list)):
            # each section has following keys
            section_info = get_section_info(idx, sectionID_list, type_list, time_list, day_list)
            # if section info is complete, add to the course_sections
            if check_section_info_integrity(section_info):
                course_sections.append(section_info)
            # print(temp)

            print("{:20}".format(get_section_number(sectionID_list[idx].text)),
                  "{:20}".format(type_list[idx].text),
                  "{:20}".format(str(get_section_time(time_list[idx].text)[0])),
                  "{:20}".format(str(get_section_time(time_list[idx].text)[1])),
                  "{:30}".format(str(get_section_day(day_list[idx].text))),
                  (day_list[idx].text))

        # add course into department dict
        course_id = str(get_course_department(course_description)) + str(get_course_number(course_description))
        course_dict = dict()
        course_dict["CourseID"] = course_id
        course_dict["Sections"] = course_sections
        # print(course_dict)

        course_list.append(course_dict)
        # print(course_list)

    department_dict["DepartmentName"] = department_name
    department_dict["Courses"] = course_list
    # print(result_dict)
    return department_dict


def get_expected_class_size(url):
    new_context = ssl._create_unverified_context()
    page = urllib.request.urlopen(url,
                                  context=new_context)
    soup = BeautifulSoup(page.read(), "html.parser")
    registerStudentsList = []
    # get all the courses on the page
    for course in soup.find_all('div', class_="course-info"):
        # focus on specific class level
        courseDescription = course.find('a', class_='courselink').text
        if (get_course_number(courseDescription) >= CLASS_LEVEL_LIMIT):
            continue

        # section information
        sectionList = course.find('table', class_="sections responsive")
        registerList = sectionList.find_all('td', class_="registered")
        typeList = sectionList.find_all('td', class_="type")
        for sectionIndex in range(len(registerList)):
            # because type and registered are bijective, so we can use same index
            # also, we only count for lecture
            if (typeList[sectionIndex].text == "Lecture"):
                registerStudentsList.append(registerList[sectionIndex].text)

    for index in range(len(registerStudentsList)):
        registerStudentsList[index] = get_num_students_in_class(registerStudentsList[index])
    return get_students_experience(registerStudentsList)


def concat_department_dict(department_dict_list):
    # concat several department result into school dict
    pass


def get_school_dict(department_list, school_name="USC"):
    school_dict = dict()
    school_dict["SchoolName"] = school_name
    school_dict["Departments"] = department_list
    return {"Schools": [school_dict]}


def main():
    # open the page
    programsURL = {"urlCSCI": "http://classes.usc.edu/term-20183/classes/csci",
                   "urlITP": "http://classes.usc.edu/term-20183/classes/itp/",
                   "urlMATH": "http://classes.usc.edu/term-20183/classes/math",
                   "urlPHYS": "http://classes.usc.edu/term-20183/classes/phys/",
                   "urlWRIT": "http://classes.usc.edu/term-20183/classes/writ",
                   }
    programsURL = {"urlWRIT": "http://classes.usc.edu/term-20183/classes/writ",
                   "urlCSCI": "http://classes.usc.edu/term-20191/classes/csci"}
    department_list = list()
    for programURL in programsURL:
        # print("Students expected class size <" + str(CLASS_LEVEL_LIMIT) + " level in", programURL, "is:\t",
        #       str(getExpectedClassSize(programsURL[programURL])))
        department_dict = get_department_courses_list(programsURL[programURL])
        department_list.append(department_dict)
        # print(str(result_dict))
    school_dict = get_school_dict(department_list, "USC")
    with open("result.json", "w") as file:
        json.dump(school_dict, file, indent=4)
    print()
    print("complete writing json...")


def check_json_existence(file_path):
    '''
    by specifying the file address,
    open the file to see whether it has corresponding class information
    :param file_path:
    :return: boolean
    '''
    with open(file_path, mode="r") as file:
        lines = file.readlines()
        if len(lines) > 100:
            return True
        else:
            return False


def check_time_conversion(time_text):
    '''
    manually check to see whether we get the same time
    :param time_text: text we got after doing web scraping
    :return:
    '''
    time = datetime.strptime(time_text, "%H:%M")
    time.strftime("%I:%M %p")
    print(time)


def check_day_conversion(day_list):
    '''
    manually check to see whether we got the conversion correct
    :param day_list: a list of day, such as [1,2] means Mon & Tue
    :return:
    '''
    for day in day_list:
        print(DAY_NAMES[day - 1], end=" ")
    print()

def test_json_integrity(json_file_path):
    '''
    check to see whether the json format correctly
    :param json_file_path: json file path
    :return: boolean
    '''
    try:
        with open(json_file_path, 'r') as file:
            load_dict = json.load(file)
            print(load_dict)
            return True
    except:
        print("Error in open the file")
        return False




if __name__ == "__main__":
    main()
# todo how to store empty class into the database
# todo cross list class will be dealt with Luz
