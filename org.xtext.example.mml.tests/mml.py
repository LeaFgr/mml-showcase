import pandas as pd
mml_data = pd.read_csv('iris.csv', sep=',')

print (mml_data)
Y = mml_data.iloc[:,-1]
X = mml_data.iloc[:,:-1]
from sklearn.tree import DecisionTreeClassifier 
classifier = DecisionTreeClassifier(max_depth=None)
from sklearn.model_selection import StratifiedKFold
skf = StratifiedKFold(n_splits=2, shuffle=True)
for index, (train_indices, val_indices) in enumerate(skf.split(X, Y)):
    print('Training on fold :' + str(index+1))
    x_train, x_test = X[train_indices], X[val_indices]
    classifier.fit(x_train,y_train)
    y_train, y_test = Y[train_indices], Y[val_indices]
    y_pred = classifier.predict(x_test)
    sklearn.metrics.recall_score(y_test, y_pred, labels=None, pos_label=1, average='binary', sample_weight=None)
    sklearn.metrics.f1_score(y_test, y_pred, labels=None, pos_label=1, average='binary', sample_weight=None)

