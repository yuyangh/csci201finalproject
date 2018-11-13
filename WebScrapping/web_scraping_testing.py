import json
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