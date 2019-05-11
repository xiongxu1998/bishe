import matplotlib.pyplot as plt
import numpy as np
ax=plt.gca()
[ax.spines[i].set_visible(False) for i in ["top","right"]]

def gatt(m,t):
    for j in range(len(m)):
        y = t[j]
        x = m[j][2] - m[j][1]
        plt.barh(y, x ,left=(m[j][1]))
        plt.text(m[j][1]+x/8,y,'id:%s\n'%(m[j][0]),color="white",size=8)

if __name__=="__main__":
    file1 = open("hello.txt","r")
    file2 = open("result.txt","r")
    flightList = []
    resultList = []
    listM = []
    s = []
    for line in file1:
        line = line.strip()
        flightList = line.split(" ")
        flightList[0] = int(flightList[0])
        s = flightList[1].split(":")
        flightList[1] = float(s[0]) + float(s[1])/60
        s = flightList[2].split(":")
        flightList[2] = float(s[0]) + float(s[1])/60
        listM.append(flightList)
    for line in file2:
        resultList = line.strip().split(" ")
        for index in range(len(resultList)):
            resultList[index] = int(resultList[index])
    gatt(listM,resultList)
    plt.yticks(np.arange(max(resultList)),np.arange(1,max(resultList)+1))
    plt.show()




