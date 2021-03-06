package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.AllVariables;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CSVSeparator;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;
import org.xtext.example.mydsl.mml.SVMClassification;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.XFormula;

import com.google.common.io.Files;
import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;

	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo.csv\" separator ,\n" + "mlframework scikit-learn\n"
				+ "algorithm DT\n" + "CrossValidation { numRepetitionCross 2 }\n" + "recall F1\n" + "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());

	}

	@Test
	public void compileDataInput() throws Exception {
		// MMLModel result = parseHelper.parse("datainput \"foo2.csv\" separator ;\n"
		// + "mlframework scikit-learn\n"
		// + "algorithm DT\n"
		// + "TrainingTest { pourcentageTraining 70 }\n"
		// + "recall\n"
		// + "");
		// MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
		// + "mlframework scikit-learn\n"
		// + "algorithm DT\n"
		// + "CrossValidation { numRepetitionCross 2 }\n"
		// + "recall F1\n"
		// + "");
		File folder = new File("inputmml/");
		File[] listOfFiles = folder.listFiles();

		for (int f = 0; f < listOfFiles.length; f++) {
			if (listOfFiles[f].isFile()) {
				Scanner scanner = new Scanner(Paths.get("inputmml/" + listOfFiles[f].getName()),
						StandardCharsets.UTF_8.name());
				String content = scanner.useDelimiter("\\A").next();
				System.out.println(content);
				scanner.close();
				
				
				System.out.println("avant parsing");
				MMLModel result = parseHelper.parse(content);
				Assertions.assertNotNull(result);
				EList<Resource.Diagnostic> errors = result.eResource().getErrors();
				System.out.println(errors);
				Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");
				//Assertions.assertEquals("iris.csv", result.getInput().getFilelocation());
				System.out.println("après parsing");
				

				DataInput dataInput = result.getInput();
				String fileLocation = dataInput.getFilelocation();

				MLChoiceAlgorithm mlAlgorithm = result.getAlgorithm();
				MLAlgorithm algo = mlAlgorithm.getAlgorithm();
				FrameworkLang frmwk = mlAlgorithm.getFramework();
				CSVSeparator DEFAULT_COLUMN_SEPARATOR = CSVSeparator.COMMA; // by default
				CSVSeparator csv_separator = DEFAULT_COLUMN_SEPARATOR;
				CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
				if (parsingInstruction != null) {
					System.out.println("parsing instruction..." + parsingInstruction);
					csv_separator = parsingInstruction.getSep();
					if (CSVSeparator.SEMI_COLON.equals(csv_separator)) {
						csv_separator = CSVSeparator.SEMI_COLON;
					} else if (CSVSeparator.COMMA.equals(csv_separator)) {
						csv_separator = CSVSeparator.COMMA;
					}
				}
				switch (frmwk) {

				case SCIKIT:

					String pythonImport = "import pandas as pd\n";
					pythonImport += "import sklearn\n";
					String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep="
							+ mkValueInSingleQuote(csv_separator.getLiteral()) + ")\n";
					String pandasCode = pythonImport + csvReading;

					String algoML = "";
					System.out.println("algo");
					if (algo instanceof SVM) {
						System.out.println("SVM");
						algoML = "from sklearn import svm\n";
						SVM svm = (SVM) algo;
						String gamma = svm.getGamma();
						if(gamma == null) {
							System.out.println("gamma non specifie");
							gamma = "auto_deprecated";
						}
						String C = svm.getC();
						if(C == null) {
							System.out.println("C non specifie");
							C = "1.0";
							
						}
						SVMKernel kernel = svm.getKernel();
						String resKernel="";
						if(kernel == null) { //pas de SVM classification spécifié
							System.out.println("SVMClassification non specifiee");
							resKernel = "rbf";
						}else {//
							System.out.println("SVMClassification specifiee");
							if (SVMKernel.LINEAR.equals(kernel)) {
								kernel = SVMKernel.LINEAR;
							} else if (SVMKernel.POLY.equals(kernel)) {
								kernel = SVMKernel.POLY;
								
							}else if (SVMKernel.RADIAL.equals(kernel)) {
								kernel = SVMKernel.RADIAL;
							}
							resKernel = kernel.getLiteral();
						}
						SVMClassification svmClass = svm.getSvmclassification();
						String resClass="";
						if(svmClass == null) { //pas de SVM classification spécifié
							System.out.println("SVMClassification non specifiee");
							resClass="";
						}else {//
							System.out.println("SVMClassification specifiee");
							if (SVMClassification.CCLASS.equals(svmClass)) {
								svmClass = SVMClassification.CCLASS;
							} else if (SVMClassification.NU_CLASS.equals(svmClass)) {
								svmClass = SVMClassification.NU_CLASS;
							}else if (SVMClassification.ONE_CLASS.equals(svmClass)) {
								svmClass = SVMClassification.ONE_CLASS;
							}
							resClass = svmClass.getLiteral();
						}
						algoML += "classifier = svm.SVC(gamma= " + gamma+",kernel='"+resKernel+"',C="+C+")\n";
						
					} else if (algo instanceof DT) {
						System.out.println("DT");
						DT dt = (DT) algo;
						String max_depth;
						if (dt.getMax_depth() != 0) {
							max_depth = Integer.toString(dt.getMax_depth());
						} else {
							max_depth = "None";
						}
						algoML = "from sklearn.tree import DecisionTreeClassifier \nclassifier = DecisionTreeClassifier(max_depth="
								+ max_depth + ")\n";
					} else if (algo instanceof LogisticRegression) {
						System.out.println("LogisticRegression");
						LogisticRegression lr = (LogisticRegression) algo;
						algoML = "from sklearn.linear_model import LogisticRegression\n";
						algoML += "classifier = LogisticRegression(random_state=0, solver='lbfgs', multi_class='multinomial')\n";

					} else if (algo instanceof RandomForest) {
						System.out.println("RandomForest");
						RandomForest rf = (RandomForest) algo;
						algoML = "from sklearn.ensemble import RandomForestClassifier\n";
						algoML += "classifier = RandomForestClassifier()\n";
					}
					System.out.println("Formule");
					RFormula formula = result.getFormula();
					String codeFormulaX = "";
					String codeFormulaY = "";
					if (formula == null) { // si aucune formule n'a été donnée
						codeFormulaY = "Y = mml_data.iloc[:,-1]\n";
						codeFormulaX = "X = mml_data.iloc[:,:-1]\n";
					} else {// une formule a été donnée
						System.out.println("Formule donnee");
						FormulaItem y = formula.getPredictive();
						XFormula x = formula.getPredictors();
						if (y == null) { // si la predictive n'a pas été renseignée
							System.out.println("Predictive non renseignee");
							codeFormulaY = "Y = mml_data.iloc[:,-1]\n";
							if (x instanceof AllVariables) {// toutes les variables sont explicatives
								codeFormulaX = "X = mml_data.iloc[:, :-1] \n";
							} else {// si PredictorVariables
								PredictorVariables xPred = (PredictorVariables) x;
								// on initialise codeFormulaX
								if (xPred.getVars().get(0).getColName() != null) {// c'est un string
									codeFormulaX = "Y = mml_data[[" + xPred.getVars().get(0).getColName();
								} else {// c'est un entier
									codeFormulaX = "X = mml_data.iloc[:,[" + xPred.getVars().get(0).getColumn();
								}
								for (int i = 1; i < xPred.getVars().size(); i++) {
									// xPred.getVars().get(i)
									if (xPred.getVars().get(i).getColName() != null) {// c'est un string
										codeFormulaX += ",";
										codeFormulaX += xPred.getVars().get(i).getColName();
									} else {// c'est un entier
										codeFormulaX += ",";
										codeFormulaX += xPred.getVars().get(i).getColumn();
									}

								}
								if (xPred.getVars().get(0).getColName() != null) {// c'est un string
									codeFormulaX += "]] \n";
								} else {// c'est un entier
									codeFormulaX += "]] \n";
								}

							}
						} else {// la prédictive a été donnée
							System.out.println("Predictive renseignee");
							if (y.getColName() != null) { // si colname est un string
								System.out.println("Predictive est un string");
								codeFormulaY = "Y = mml_data['" + y.getColName() + "'] \n";
								if (x instanceof AllVariables) {
									System.out.println("Toutes les variables sont explicatives");
									codeFormulaX = "X = mml_data.loc[:, ~mml_data.columns.isin(['"+ y.getColName() +"'])]\n";
								}
							} else { //// si colname est un int
								System.out.println("Predictive est un int");
								codeFormulaY = "Y = mml_data.iloc[:," + y.getColumn() + "] \n";
								if (x instanceof AllVariables) {
									System.out.println("Toutes les variables sont explicatives");
									codeFormulaX += "X = mml_data\n";
									codeFormulaX += "X.drop(X.columns["+y.getColumn()+"],axis=1,inplace=True)\n";
									  
								}
							}
								if (x instanceof PredictorVariables) {
									// PredictorVariables
									System.out.println("Des variables explicatives ont été données");
									PredictorVariables xPred = (PredictorVariables) x;
									// on initialise codeFormulaX
									if (xPred.getVars().get(0).getColName() != null) {// c'est un string
										System.out.println("Les variables explicatives sont des strings");
										codeFormulaX = "X = mml_data[['" + xPred.getVars().get(0).getColName()+"'";
									} else {// c'est un entier
										System.out.println("Les variables explicatives sont des entiers");
										codeFormulaX = "X = mml_data.iloc[:,[" + xPred.getVars().get(0).getColumn();
									}
									for (int i = 1; i < xPred.getVars().size(); i++) {
										// xPred.getVars().get(i)
										if (xPred.getVars().get(i).getColName() != null) {// c'est un string
											codeFormulaX += ",'";
											codeFormulaX += xPred.getVars().get(i).getColName()+"'";
										} else {// c'est un entier
											codeFormulaX += ",";
											codeFormulaX += xPred.getVars().get(i).getColumn();
										}

									}
									if (xPred.getVars().get(0).getColName() != null) {// c'est un string
										codeFormulaX += "]] \n";
									} else {// c'est un entier
										codeFormulaX += "]] \n";
									}

								}


						}
					}
					System.out.println("Validation");
					/* LA VALIDATION */
					Validation validation = result.getValidation();
					StratificationMethod stratification = validation.getStratification();
					// LA METRIQUE
					// ValidationMetric metric = validation;
					String metricCode = "";
					EList<ValidationMetric> metric = validation.getMetric();
					System.out.println("Metrique");
					for (int i = 0; i < metric.size(); i++) {
						if (ValidationMetric.RECALL.equals(metric.get(i))) {
							metricCode += "print('recall = ')\n";
							metricCode += "print(recall)\n";
						} else if (ValidationMetric.PRECISION.equals(metric.get(i))) {
							metricCode += "print('precision = ')\n";
							metricCode += "print(precision)\n";
						} else if (ValidationMetric.F1.equals(metric.get(i))) {
							metricCode += "print('f1_score = ')\n";
							metricCode += "print(f1_score)\n";
						}
					}

					// LA STRATIFICATION
					String stratificationCode = "";
					System.out.println("Stratification");
					if (stratification instanceof CrossValidation) {// c'est de la crossValidation
						System.out.println("CrossValidation");
						CrossValidation cross = (CrossValidation) stratification;
						int numRep = cross.getNumber();
						// créer le x_train et le y_train
						stratificationCode += "from sklearn.model_selection import StratifiedKFold\n";
						stratificationCode += "skf = StratifiedKFold(n_splits=" + numRep + ", shuffle=True)\n";
						stratificationCode += "recall = 0\n";
						stratificationCode += "f1_score = 0\n";
						stratificationCode += "precision = 0\n";
						stratificationCode += "for train_indices, val_indices in skf.split(X, Y):\n";
						stratificationCode += "    x_train, x_test = X.iloc[train_indices,], X.iloc[val_indices,]\n";
						stratificationCode += "    y_train, y_test = Y[train_indices], Y[val_indices]\n";
						stratificationCode += "    classifier.fit(x_train,y_train)\n";
						stratificationCode += "    y_pred = classifier.predict(x_test)\n";
						stratificationCode += "    recall = recall + sklearn.metrics.recall_score(y_test, y_pred, labels=None, pos_label=1, average=\'macro\')\n";
						stratificationCode += "    precision = precision + sklearn.metrics.precision_score(y_test, y_pred, labels=None, pos_label=1, average=\'macro\')\n";
						stratificationCode += "    f1_score = f1_score + sklearn.metrics.f1_score(y_test, y_pred, labels=None, pos_label=1, average=\'macro\')\n";
						stratificationCode += "recall = recall /" + numRep + "\n";
						stratificationCode += "f1_score = f1_score /" + numRep + "\n";
						stratificationCode += "precision = precision /" + numRep + "\n";

						stratificationCode += metricCode + "\n";

						///////////// REVOIR CROSS VALIDATION
					} else {// c'est du TrainingTest
						System.out.println("TrainingTest");
						TrainingTest trainTest = (TrainingTest) stratification;
						int pourcentageTraining = trainTest.getNumber();
						double pourcentage = (100 - pourcentageTraining * 1.0 )/100;
						stratificationCode += "from sklearn.model_selection import train_test_split\n";
						stratificationCode += "x_train, x_test, y_train, y_test = train_test_split(X, Y, test_size="
								+ pourcentage + ")\n";
						stratificationCode += "classifier.fit(x_train,y_train)\n";
						stratificationCode += "y_pred = classifier.predict(x_test)\n";
						stratificationCode += "recall = sklearn.metrics.recall_score(y_test, y_pred, labels=None, pos_label=1, average=\'macro\')\n";
						stratificationCode += "precision = sklearn.metrics.precision_score(y_test, y_pred, labels=None, pos_label=1, average=\'macro\')\n";
						stratificationCode += "f1_score = sklearn.metrics.f1_score(y_test, y_pred, labels=None, pos_label=1, average=\'macro\')\n";


						stratificationCode += metricCode;
					}

					// variable predicitive par defaut est la derniere du tableau
					// FrameworkLang framework = mlAlgorithm.getFramework();
					pandasCode += "\nprint (mml_data)\n";
					pandasCode += codeFormulaY;
					pandasCode += codeFormulaX;
					pandasCode += algoML;
					pandasCode += stratificationCode;

					System.out.println(pandasCode);
					Files.write(pandasCode.getBytes(), new File("mml.py"));
					// end of Python generation

					/*
					 * Calling generated Python script (basic solution through systems call) we
					 * assume that "python" is in the path
					 */
					Process p = Runtime.getRuntime().exec("python3.5 mml.py");
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
					}
					break;
					
				case R: 
					String RcsvReading = "mml_data <- read.csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator.getLiteral()) + ")\n";
					String RCode = RcsvReading;

					String RalgoML = "";
					System.out.println("algo");
					if (algo instanceof SVM) {
						System.out.println("SVM");
						SVM svm = (SVM) algo;
						String gamma = svm.getGamma();
						if(gamma == null) {
							System.out.println("gamma non specifie");
							gamma = "1 / ncol(train)" ;
						}
						String C = svm.getC();
						if(C == null) {
							System.out.println("C non specifie");
							C = "1";
							
						}
						SVMKernel kernel = svm.getKernel();
						String resKernel="";
						if(kernel == null) { //pas de SVM classification spécifié
							System.out.println("SVMClassification non specifiee");
							resKernel = "linear";
						}else {//
							System.out.println("SVMClassification specifiee");
							if (SVMKernel.LINEAR.equals(kernel)) {
								kernel = SVMKernel.LINEAR;
							} else if (SVMKernel.POLY.equals(kernel)) {
								kernel = SVMKernel.POLY;
								
							}else if (SVMKernel.RADIAL.equals(kernel)) {
								kernel = SVMKernel.RADIAL;
							}
							resKernel = kernel.getLiteral();
						}
						SVMClassification svmClass = svm.getSvmclassification();
						String resClass="C-classification";
						if(svmClass == null) { //pas de SVM classification spécifié
							System.out.println("SVMClassification non specifiee");
						}else {//
							System.out.println("SVMClassification specifiee");
							if (SVMClassification.CCLASS.equals(svmClass)) {
								svmClass = SVMClassification.CCLASS;
							} else if (SVMClassification.NU_CLASS.equals(svmClass)) {
								svmClass = SVMClassification.NU_CLASS;
							}else if (SVMClassification.ONE_CLASS.equals(svmClass)) {
								svmClass = SVMClassification.ONE_CLASS;
							}
							resClass = svmClass.getLiteral();
						}
						//RalgoML += "install.packages(\'e1071\')\n";
						RalgoML += "library(e1071)\n";
						RalgoML += "classifier <- svm(formule,data=train,kernel=\'"+resKernel+"\',type = \'"+resClass+"\' , gamma="+gamma+",cost = "+C+")\n";
						
					} else if (algo instanceof DT) {
						System.out.println("DT");
						DT dt = (DT) algo;
						String max_depth;
						//RalgoML += "install.packages(\'rpart\')\n";
						RalgoML += "library(rpart)\n";
						if (dt.getMax_depth() != 0) {
							max_depth = Integer.toString(dt.getMax_depth());
							RalgoML += "ctrl <- rpart.control(maxdepth = "+max_depth+")\n";

						} else {
							RalgoML += "ctrl <- rpart.control()\n";
						}
						RalgoML += "classifier <- rpart(formule, method=\"class\", data=train,control = ctrl)\n";

					} else if (algo instanceof LogisticRegression) {
						System.out.println("LogisticRegression");
						LogisticRegression lr = (LogisticRegression) algo;
						RalgoML += "install.packages(\'nnet\')\n";
						RalgoML = "library(nnet)\n";
						RalgoML += "classifier <- multinom(formule, data = train)\n";

					} else if (algo instanceof RandomForest) {
						System.out.println("RandomForest");
						RandomForest rf = (RandomForest) algo;
						//RalgoML += "install.packages(\'randomForest\')\n";
						RalgoML = "library(randomForest)\n";
						RalgoML += "classifier <- randomForest(formule, data = train)\n";
					}
					System.out.println("Formule");
					RFormula formulaR = result.getFormula();
					String nomY = "";
					String nomsX = "";
					String nbPredictive = "";
					if (formulaR == null) { // si aucune formule n'a été donnée
						nomY = "nomY = colnames(mml_data)[ncol(mml_data)]\n";
						nbPredictive = "nbPredictive = ncol(mml_data)\n";
						nomsX = "nomsX = \'.\'\n";
					} else {// une formule a été donnée
						System.out.println("Formule donnee");
						FormulaItem y = formulaR.getPredictive();
						XFormula x = formulaR.getPredictors();
						if (y == null) { // si la predictive n'a pas été renseignée
							System.out.println("Predictive non renseignee");
							nomY = "nomY = colnames(mml_data)[ncol(mml_data)]\n";
							nbPredictive = "nbPredictive = ncol(mml_data)\n";
							if (x instanceof AllVariables) {// toutes les variables sont explicatives
								nomsX = "nomsX = \'.\'\n";
							} else {// si PredictorVariables
								PredictorVariables xPred = (PredictorVariables) x;
								// on initialise codeFormulaX
								if (xPred.getVars().get(0).getColName() != null) {// c'est un string
									nomsX = "nomsX = paste(\'"+xPred.getVars().get(0).getColName()+"\'";
								} else {// c'est un entier
									nomsX  = "nomsX = paste(colnames(mml_data)[" + xPred.getVars().get(0).getColumn()+"]";
								}
								for (int i = 1; i < xPred.getVars().size(); i++) {
									// xPred.getVars().get(i)
									if (xPred.getVars().get(i).getColName() != null) {// c'est un string
										nomsX += ",\' + \',";
										nomsX += "\'" + xPred.getVars().get(i).getColName()+"\'";
									} else {// c'est un entier
										nomsX += ",\' + \',";
										nomsX += "colnames(mml_data)[" + xPred.getVars().get(i).getColumn()+"]";
									}

								}
								if (xPred.getVars().get(0).getColName() != null) {// c'est un string
									nomsX += ")\n";
								} else {// c'est un entier
									nomsX += ")\n";
								}

							}
						} else {// la prédictive a été donnée
							System.out.println("Predictive renseignee");
							if (y.getColName() != null) { // si colname est un string
								System.out.println("Predictive est un string");
								nomY = "nomY = \'"+y.getColName()+"\'\n";
								nbPredictive = "nbPredictive = which(colnames(mml_data) == \'"+y.getColName()+"\')\n";
								if (x instanceof AllVariables) {
									System.out.println("Toutes les variables sont explicatives");
									nomsX = "nomsX = \'.\'\n";
								}
							} else { //// si colname est un int
								System.out.println("Predictive est un int");
								nomY = "nomY = colnames(mml_data)[" +y.getColumn()+"] \n";
								nbPredictive = "nbPredictive = "+y.getColumn()+"\n";

								if (x instanceof AllVariables) {
									System.out.println("Toutes les variables sont explicatives");
									nomsX = "nomsX = \'.\'\n";									  
								}
							}
								if (x instanceof PredictorVariables) {
									// PredictorVariables
									System.out.println("Des variables explicatives ont été données");
									PredictorVariables xPred = (PredictorVariables) x;
									// on initialise codeFormulaX
									if (xPred.getVars().get(0).getColName() != null) {// c'est un string
										nomsX = "nomsX = paste(\'"+xPred.getVars().get(0).getColName()+"\'";
									} else {// c'est un entier
										nomsX  = "nomsX = paste(colnames(mml_data)[" + xPred.getVars().get(0).getColumn()+"]";
									}
									for (int i = 1; i < xPred.getVars().size(); i++) {
										// xPred.getVars().get(i)
										if (xPred.getVars().get(i).getColName() != null) {// c'est un string
											nomsX += ",\' + \',";
											nomsX += "\'" + xPred.getVars().get(i).getColName()+"\'";
										} else {// c'est un entier
											nomsX += ",\' + \',";
											nomsX += "colnames(mml_data)[" + xPred.getVars().get(i).getColumn()+"]";
										}

									}
									if (xPred.getVars().get(0).getColName() != null) {// c'est un string
										nomsX += ")\n";
									} else {// c'est un entier
										nomsX += ")\n";
									}

								}


						}
					}
					String formuleComplete = nomsX + nomY + "formule = paste(nomY, \'~\',nomsX)\n";
					formuleComplete += "formule = as.formula(formule)\n";

					System.out.println("Validation");
					/* LA VALIDATION */
					Validation validationR = result.getValidation();
					StratificationMethod stratificationR = validationR.getStratification();
					// LA METRIQUE
					// ValidationMetric metric = validation;


					EList<ValidationMetric> metricR = validationR.getMetric();
					String metricCodeR = "";
					System.out.println("Metrique");
					for (int i = 0; i < metricR.size(); i++) {
						if (ValidationMetric.RECALL.equals(metricR.get(i))) {
							metricCodeR += "print(paste(\"Recall = \",recall))\n";
						} else if (ValidationMetric.PRECISION.equals(metricR.get(i))) {
							metricCodeR += "print(paste(\"Precision = \",precision))\n";
						} else if (ValidationMetric.F1.equals(metricR.get(i))) {
							metricCodeR += "print(paste(\"f1_score = \",f1))\n";
						}
					}

					// LA STRATIFICATION
					String RstratificationCode = "library(MLmetrics)\n";
					System.out.println("Stratification");
					if (stratificationR instanceof CrossValidation) {// c'est de la crossValidation
						System.out.println("CrossValidation");
						CrossValidation cross = (CrossValidation) stratificationR;
						int numRep = cross.getNumber();
						// créer le x_train et le y_train
						RstratificationCode += "library(caret)\n";
						RstratificationCode += "folds <- createFolds(factor(mml_data$variety), k = " + numRep + ", list = TRUE, returnTrain = TRUE)\n";
						RstratificationCode += "f1 <- 0\n";
						RstratificationCode += "recall <- 0\n";
						RstratificationCode += "precision <- 0\n";
						RstratificationCode += "for(test_ind in folds){\n";
						RstratificationCode += "train <- mml_data[-test_ind, ]\n";
						RstratificationCode += "test <- mml_data[test_ind, ]\n";
						RstratificationCode += "x_test <- test[,-nbPredictive]\n";
						RstratificationCode += "y_test <- test[,nbPredictive]\n";
						RstratificationCode += RalgoML;
						RstratificationCode += "ypred <- predict(classifier,test,type='class')\n";
						RstratificationCode += "f1 <- f1 + F1_Score(y_test, ypred, positive = NULL)\n";
						RstratificationCode += "recall <- recall + Recall(y_test, ypred, positive = NULL)\n";
						RstratificationCode += "precision <- precision + Precision(y_test, ypred, positive = NULL)\n";
						RstratificationCode += "}\n";
						RstratificationCode += "recall = recall/length(folds)\n";
						RstratificationCode += "f1 = f1/length(folds)\n";
						RstratificationCode += "precision = f1/length(folds)\n";

					} else {// c'est du TrainingTest
						System.out.println("TrainingTest");
						TrainingTest trainTestR = (TrainingTest) stratificationR;
						int pourcentageTraining = trainTestR.getNumber();
						double pourcentage = (100 - pourcentageTraining * 1.0 )/100;
						System.out.println(pourcentage);
						RstratificationCode += "pourcentage = "+pourcentage+"\n";
						RstratificationCode += "test_ind <- sample(seq_len(nrow(mml_data)), size = pourcentage*nrow(mml_data))\n";
						RstratificationCode += "train <- mml_data[-test_ind, ]\n";
						RstratificationCode += "test <- mml_data[test_ind, ]\n";
						RstratificationCode += "x_test <- test[,-nbPredictive]\n";
						RstratificationCode += "y_test <- test[,nbPredictive]\n";
						RstratificationCode += RalgoML;
						RstratificationCode += "ypred <- predict(classifier,x_test,type=\"class\")\n";
						RstratificationCode += "f1 <- F1_Score(y_test, ypred, positive = NULL)\n";
						RstratificationCode += "recall <- Recall(y_test, ypred, positive = NULL)\n";
						RstratificationCode += " precision <- Precision(y_test, ypred, positive = NULL)\n";

					}

					// variable predicitive par defaut est la derniere du tableau
					// FrameworkLang framework = mlAlgorithm.getFramework();
					RCode += "\nprint (mml_data)\n";
					RCode += formuleComplete;
					RCode += nbPredictive;
					RCode += RstratificationCode;
					RCode += metricCodeR;
					

					System.out.println(RCode);
					Files.write(RCode.getBytes(), new File("mml.R"));
					// end of R generation

					/*
					 * Calling generated R script (basic solution through systems call) we
					 * assume that "R" is in the path
					 */
					Process pR = Runtime.getRuntime().exec("Rscript mml.R");
					BufferedReader inR = new BufferedReader(new InputStreamReader(pR.getInputStream()));
					String lineR;
					while ((lineR = inR.readLine()) != null) {
						System.out.println(lineR);
					}
					break;
					
//				case R:
//					String readCsv = "mml_data <- read.csv(" + mkValueInSingleQuote(fileLocation) + ", sep="
//							+ mkValueInSingleQuote(csv_separator.getLiteral()) + ")\n";
//					String codeR = "" + readCsv;
//					RFormula formulaR = result.getFormula();
//					System.out.println("formule R : ");
//					if(algo instanceof RandomForest) {
//						codeR += "library(randomForest)\n"
//								+ "modelRF <- randomForest(formula=" + formulaR.toString() + ",data=mml_data)\n"
//										+ "print(modelRF)";
//					}
//					Files.write(codeR.getBytes(), new File("mml.R"));
//					// end of R generation
//
//					/*
//					 * Calling generated R script (basic solution through systems call) we
//					 * assume that "R" is in the path
//					 */
//					Process pR = Runtime.getRuntime().exec("Rscript mml.R");
//					BufferedReader inR = new BufferedReader(new InputStreamReader(pR.getInputStream()));
//					String lineR;
//					while ((lineR = inR.readLine()) != null) {
//						System.out.println(lineR);
//					}
//					break;
				}
				System.out.println("FIN");
			}
		}

	}

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}

}

