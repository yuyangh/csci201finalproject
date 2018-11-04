import json

from bs4 import BeautifulSoup
from datetime import datetime, timedelta
import urllib.request
import ssl

# only focus on undergraduate
CLASS_LEVEL_LIMIT = 500
DAY_ABBREVIATIONS = ["Mon", "Tue", "Wed", "Thu", "Fri"]
DAY_NAMES = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]


# turn test into int
def getUnits(units):
    return int(units[1:2])


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
    return section_number[:-1]


def get_section_day(day_text):
    '''

    :param day_text: day description on classes.usc
    :return: a list containing the day of the section in number, such as [1,2] is Mon and Tue
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
        else:
            for index in range(0, len(DAY_NAMES)):
                if days[0] == DAY_NAMES[index]:
                    day.append(index + 1)
            return day
    # if there are 2 word
    if len(days) == 2:
        for i in range(0, len(days)):
            for index in range(0, len(DAY_ABBREVIATIONS)):
                if days[i] == DAY_ABBREVIATIONS[index]:
                    day.append(index + 1)
                    break
        return day
    return day


def get_units_list(soup):
    '''
    get the range of the units
    :param soup: beautiful soup object
    :return:
    '''
    unitsList = []
    for course in soup.find_all('div', class_='course-id'):
        for units in course.find_all('span', class_='units'):
            if (getUnits(units.text) not in unitsList):
                unitsList.append(getUnits(units.text))
    unitsList.sort()
    return unitsList


def get_num_students_in_class(registered):
    pos = registered.find("of")
    if (pos < 0):
        return 0
    return int(registered[:pos])


def get_students_experience(registeredList):
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


def get_department_courses(url):
    new_context = ssl._create_unverified_context()
    page = urllib.request.urlopen(url,context=new_context)
    soup = BeautifulSoup(page.read(), "html.parser")

    department_set = dict()
    result_set = dict()
    # get department name
    department_name = soup.find("abbr").text

    # go through each course
    for course in soup.find_all('div', class_="course-info"):
        # course_description: CSCI 104L: Data Structures and Object Oriented Design (4.0 units)
        course_description = course.find('a', class_='courselink').text
        print()
        # print(course_description)

        print(get_course_department(course_description), get_course_number(course_description),
              get_course_units(course_description))

        section_list = course.find('table', class_="sections responsive")
        # print(section_list)
        sectionID_list = course.find_all('td', class_="section")
        time_list = section_list.find_all('td', class_="time")
        type_list = section_list.find_all('td', class_="type")
        day_list = section_list.find_all('td', class_="days")

        course_sections=list()
        for idx in range(len(type_list)):
            section_info = dict()
            section_info["sectionID"] = get_section_number(sectionID_list[idx].text)
            section_info["type"] = type_list[idx].text
            section_info["startTime"] = str(get_section_time(time_list[idx].text)[0])
            section_info["endTime"] = str(get_section_time(time_list[idx].text)[1])
            section_info["days"] = get_section_day(day_list[idx].text)
            course_sections.append(section_info)
            # print(temp)

            print("{:20}".format(get_section_number(sectionID_list[idx].text)),
                  "{:20}".format(type_list[idx].text),
                  "{:20}".format(str(get_section_time(time_list[idx].text)[0])),
                  "{:20}".format(str(get_section_time(time_list[idx].text)[1])),
                  "{:30}".format(str(get_section_day(day_list[idx].text))),
                  (day_list[idx].text))
        department_set[
            str(get_course_department(course_description)) + str(get_course_number(course_description))] = course_sections
    result_set[department_name] = department_set
    return result_set


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

def concat_department_courses():
    pass

def main():
    # open the page
    programsURL = {"urlCSCI": "http://classes.usc.edu/term-20183/classes/csci",
                   "urlITP": "http://classes.usc.edu/term-20183/classes/itp/",
                   "urlMATH": "http://classes.usc.edu/term-20183/classes/math",
                   "urlPHYS": "http://classes.usc.edu/term-20183/classes/phys/",
                   "urlWRIT": "http://classes.usc.edu/term-20183/classes/writ",
                   }
    programsURL = {"urlCSCI": "http://classes.usc.edu/term-20191/classes/csci"}
    for programURL in programsURL:
        # print("Students expected class size <" + str(CLASS_LEVEL_LIMIT) + " level in", programURL, "is:\t",
        #       str(getExpectedClassSize(programsURL[programURL])))
        result_dict = get_department_courses(programsURL[programURL])
        print(str(result_dict))
        json_str = json.dumps(result_dict)
        with open("result.json", "w") as file:
            json.dump(result_dict, file, indent=4)
        print("complete writing json...")


if __name__ == "__main__":
    main()
# todo ignore class with none or -1