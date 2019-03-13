import pandas as pd
mml_data = pd.read_csv('iris.csv', sep=',')

print (mml_data)
Y = mml_data.iloc[:,-1]
X = mml_data.iloc[:,:-1]
from sklearn.tree import DecisionTreeClassifier 
classifier = DecisionTreeClassifier(max_depth=None)
from sklearn.model_selection import train_test_split
x_train, x_test, y_train, y_test = train_test_split(X, Y, test_size=70)
classifier.fit(x_train,y_train)
y_pred = classifier.predict(x_test)
sklearn.metrics.recall_score(y_test, y_pred, labels=None, pos_label=1, average='binary', sample_weight=None)
