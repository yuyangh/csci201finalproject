import json
import bs4
from bs4 import BeautifulSoup
from datetime import datetime, timedelta
import urllib.request
import ssl


def get_all_departments(url):
    """
    get all departments url based on one semester classes.usc link
    :param url: one semester's classes.usc link
    :return: all department url on the semester's webpage
    """
    new_context = ssl._create_unverified_context()
    page = urllib.request.urlopen(url, context=new_context)
    soup = BeautifulSoup(page.read(), "html.parser")

    department_div_list = soup.find("ul", {"id": "sortable-classes"})
    department_url_list = list()

    for department_div in department_div_list:
        if type(department_div) is bs4.element.Tag:
            # print(department_div)
            department_url = department_div.find('a')['href']
            # print(department_url)
            department_url_list.append(department_url)
    return department_url_list


if __name__ == "__main__":
    url_2019_spring = "https://classes.usc.edu/term-20191/"
    print(get_all_departments(url_2019_spring))
