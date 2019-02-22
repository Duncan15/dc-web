import pandas as pd
import numpy as np
import sys
from sklearn import svm,metrics
#,cross_validation
from sklearn.model_selection import train_test_split
from scipy import sparse
f=open("E:\\pycharm文件\\ecent\\svm\\label.csv")
iris_data=pd.read_csv(f)

from numpy import genfromtxt
def get_answer(file):
    try:
        lp=open(file,"r",encoding='utf-8')
        lines=lp.readlines()
        a = len(lines)
        if a == 0:
            print("0")
        elif a == 1:
            line = lines[0]
            b = line.strip('\n')
            b2 = b.split(",")
            results1 = list(map(int, b2))
            xxx = np.array(results1)
            x = iris_data[
                ["Button", "Image", "File", "Text", "Checkbox", "Radio", "Password", "Hidden", "Reset", "Submit",
                 "Select", "Textarea", "Object", "Label", "Legend", "Method","PositiveWord","NegativeWord"]]
            y = iris_data["Result"]
            #cross_validation.
            x_train, x_test, y_train, y_test = train_test_split(x.values, y.values, test_size=0.01)
            clf = svm.SVC(kernel='rbf', gamma=0.7, C=1)
            clf.fit(x_train, y_train)
            pre = clf.predict(x_test)

            result = clf.predict(xxx.reshape(1,18))
            print(result)
        elif a >= 1:

            # for i in lines:
            #     i2 = i.strip('\n')
            #     b = i2.split(",")
            #     results1 = list(map(int, b))
            #     print(results)
            line = lines[0]
            b = line.strip('\n')
            b2 = b.split(",")
            results1 = list(map(int, b2))
            xxx = np.array(results1)
            for i in range(1,a):
                line3=lines[i]
                # print(line3)
                b3 = line3.strip('\n')
                b4 = b3.split(",")
                results3 = list(map(int, b4))
                row=np.array(results3)
                xxx=np.row_stack((xxx,row))
            x=iris_data[["Button","Image","File","Text","Checkbox","Radio","Password","Hidden","Reset","Submit","Select","Textarea","Object","Label","Legend","Method","PositiveWord","NegativeWord"]]
            y=iris_data["Result"]
            x_train,x_test,y_train,y_test=train_test_split(x.values,y.values,test_size=0.01)
            clf=svm.SVC(kernel='rbf', gamma=0.7, C=1)
            clf.fit(x_train,y_train)
            pre=clf.predict(x_test)
            result=clf.predict(xxx)
            for i in range(0,len(result)):
                print(result[i])
    except Exception as e:
        print(e)


filespace = sys.argv[1]
get_answer(filespace)
#get_answer("C:\\Users\\27148\\Desktop\\pp3.txt")
#C:\Users\27148\Desktop

