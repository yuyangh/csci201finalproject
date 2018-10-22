from bs4 import BeautifulSoup
from datetime import datetime,timedelta
import urllib.request
import ssl

# only focus on undergraduate
CLASS_LEVEL_LIMIT = 500


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
    return course_description[course_description.find("(")+1:course_description.rfind(" ")]

def get_section_time(time_text):
    '''

    :param time_text: a string in this format "10:00-10:50am"
    :return: a tuple of start time and end time in 24 hour format
    '''
    # print(time_text)
    if time_text.find("-")==-1:
        return (None,None)
    start_time_str,end_time_str=time_text.split("-")[:2]
    suffix=time_text[-2:]

    start_time = datetime.strptime(start_time_str+" "+suffix, "%I:%M %p")
    start_time.strftime("%H:%M")
    end_time = datetime.strptime(end_time_str, "%I:%M%p")
    end_time.strftime("%H:%M")
    # adjust for 11:00-12:00pm situation
    if end_time<start_time:
        start_time-=timedelta(hours=12)
    # print("Time is:",start_time.time(),end_time.time())
    return (start_time.time(),end_time.time())


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

def get_all_courses(url):
    new_context = ssl._create_unverified_context()
    page = urllib.request.urlopen(url,
                                  context=new_context)
    soup = BeautifulSoup(page.read(), "html.parser")
    registerStudentsList = []
    for course in soup.find_all('div', class_="course-info"):
        # course_description: CSCI 104L: Data Structures and Object Oriented Design (4.0 units)
        course_description = course.find('a', class_='courselink').text
        print(get_course_department(course_description),get_course_number(course_description),get_course_units(course_description))
        print(course_description)

        section_list = course.find('table', class_="sections responsive")
        # print(section_list)
        time_list = section_list.find_all('td', class_="time")
        type_list = section_list.find_all('td', class_="type")
        day_list = section_list.find_all('td', class_="days")
        for idx in range(len(type_list)):
            print("{:30}".format(type_list[idx].text),"{:20}".format(str(get_section_time(time_list[idx].text)[0])),"{:20}".format(str(get_section_time(time_list[idx].text)[1])),"{:30}".format(day_list[idx].text))


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


def main():
    # open the page
    programsURL = {"urlCSCI": "http://classes.usc.edu/term-20183/classes/csci",
                   "urlITP": "http://classes.usc.edu/term-20183/classes/itp/",
                   "urlMATH": "http://classes.usc.edu/term-20183/classes/math",
                   "urlPHYS": "http://classes.usc.edu/term-20183/classes/phys/",
                   "urlWRIT": "http://classes.usc.edu/term-20183/classes/writ",
                   }
    programsURL = {"urlCSCI": "http://classes.usc.edu/term-20183/classes/csci"}
    for programURL in programsURL:
        # print("Students expected class size <" + str(CLASS_LEVEL_LIMIT) + " level in", programURL, "is:\t",
        #       str(getExpectedClassSize(programsURL[programURL])))
        print("Students expected class size <" + str(CLASS_LEVEL_LIMIT) + " level in", programURL, "is:\t",
            str(get_all_courses(programsURL[programURL])))

if __name__=="__main__":
    main()
