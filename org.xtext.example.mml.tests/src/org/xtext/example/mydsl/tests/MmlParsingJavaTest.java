package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

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
//		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
//				+ "mlframework scikit-learn\n"
//				+ "algorithm DT\n"
//				+ "TrainingTest { pourcentageTraining 70 }\n"
//				+ "recall\n"
//				+ "");
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "CrossValidation { numRepetitionCross 2 }\n"
				+ "recall F1\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			

	}		

	@Test
	public void compileDataInput() throws Exception {
//		MMLModel result = parseHelper.parse("datainput \"foo2.csv\" separator ;\n"
//				+ "mlframework scikit-learn\n"
//				+ "algorithm DT\n"
//				+ "TrainingTest { pourcentageTraining 70 }\n"
//				+ "recall\n"
//				+ "");
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "CrossValidation { numRepetitionCross 2 }\n"
				+ "recall F1\n"
				+ "");
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();



		String pythonImport = "import pandas as pd\n"; 
		String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		if (parsingInstruction != null) {			
			System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")\n";						
		String pandasCode = pythonImport + csvReading;


		MLChoiceAlgorithm mlAlgorithm = result.getAlgorithm();
		MLAlgorithm algo = mlAlgorithm.getAlgorithm();
		String algoML="";

		if (algo instanceof SVM) {
			//apply
			SVM svm = (SVM) algo;
		}
		else if(algo instanceof DT) {
			DT dt = (DT) algo;
			String max_depth;
			if (dt.getMax_depth() !=0 ) {
				max_depth = Integer.toString(dt.getMax_depth());
			}else {
				max_depth = "None";
			}
			algoML = "from sklearn.tree import DecisionTreeClassifier \nclassifier = DecisionTreeClassifier(max_depth=" +max_depth+")\n";
		}
		else if (algo instanceof LogisticRegression) {
			LogisticRegression lr = (LogisticRegression) algo;
		}
		else if(algo instanceof RandomForest) {
			RandomForest rf = (RandomForest) algo;
		}
	
		RFormula formula = result.getFormula();
		String codeFormulaX ="";
		String codeFormulaY ="";
		if(formula == null) { //si aucune formule n'a été donnée
			codeFormulaY = "Y = mml_data.iloc[:,-1]\n";
			codeFormulaX = "X = mml_data.iloc[:,:-1]\n";
		}else {//une formule a été donnée
			FormulaItem y = formula.getPredictive();
			XFormula x = formula.getPredictors();
			if(y == null) { //si la predictive n'a pas été renseignée
				codeFormulaY = "Y = mml_data.iloc[:,-1]";
				if(x instanceof AllVariables) {//toutes les variables sont explicatives
					codeFormulaX = "X = mml_data.iloc[:, :-1] \n";
				}else {//si PredictorVariables
					PredictorVariables xPred = (PredictorVariables) x;
					//on initialise codeFormulaX
					if(xPred.getVars().get(0).getColName() != null) {//c'est un string
						codeFormulaX = "Y = mml_data[[" + xPred.getVars().get(0).getColName();
					}else {//c'est un entier
						codeFormulaX = "X = mml_data.iloc[:," + xPred.getVars().get(0).getColumn();
					}
					for(int i=1;i<xPred.getVars().size();i++) {
						//xPred.getVars().get(i)
						if(xPred.getVars().get(i).getColName() != null) {//c'est un string
							codeFormulaX += ",";
							codeFormulaX += xPred.getVars().get(i).getColName();
						}else {//c'est un entier
							codeFormulaX += ",";
							codeFormulaX += xPred.getVars().get(i).getColumn();
						}

					}
					if(xPred.getVars().get(0).getColName() != null) {//c'est un string
						codeFormulaX += "]] \n";
					}else {//c'est un entier
						codeFormulaX += "] \n";
					}


				}
			}else {//la prédictive a été donnée
				if (y.getColName() != null) { //si colname est un string
					codeFormulaY = "Y = mml_data["+y+"] \n";
				}else { ////si colname est un int
					codeFormulaY = "Y = mml_data.iloc[:," + y + "] \n";
					if(x instanceof AllVariables) {
						codeFormulaX = "X = mml_data.iloc[:, :-" + y + ":]\n";
					} else {//PredictorVariables


						PredictorVariables xPred = (PredictorVariables) x;
						//on initialise codeFormulaX
						if(xPred.getVars().get(0).getColName() != null) {//c'est un string
							codeFormulaX = "Y = mml_data[[" + xPred.getVars().get(0).getColName();
						}else {//c'est un entier
							codeFormulaX = "X = mml_data.iloc[:," + xPred.getVars().get(0).getColumn();
						}
						for(int i=1;i<xPred.getVars().size();i++) {
							//xPred.getVars().get(i)
							if(xPred.getVars().get(i).getColName() != null) {//c'est un string
								codeFormulaX += ",";
								codeFormulaX += xPred.getVars().get(i).getColName();
							}else {//c'est un entier
								codeFormulaX += ",";
								codeFormulaX += xPred.getVars().get(i).getColumn();
							}

						}
						if(xPred.getVars().get(0).getColName() != null) {//c'est un string
							codeFormulaX += "]] \n";
						}else {//c'est un entier
							codeFormulaX += "] \n";
						}

					}

				}



			}
		}
		/* LA VALIDATION */
		Validation validation = result.getValidation();
		StratificationMethod stratification = validation.getStratification();
		//LA METRIQUE
		//ValidationMetric metric = validation;
		String metricCode ="";
		EList<ValidationMetric> metric = validation.getMetric();
		for(int i=0;i<metric.size();i++) {
			if(stratification instanceof CrossValidation) {
				metricCode +="    ";
			}
			if(ValidationMetric.RECALL.equals(metric.get(i))){
				metricCode += "sklearn.metrics.recall_score(y_test, y_pred, labels=None, pos_label=1, average=’binary’, sample_weight=None)\n";
			}else if(ValidationMetric.PRECISION.equals(metric.get(i))) {
				metricCode += "sklearn.metrics.precision_score(y_test, y_pred, labels=None, pos_label=1, average=’binary’, sample_weight=None)\n";
			}else if(ValidationMetric.F1.equals(metric.get(i))) {
				metricCode += "sklearn.metrics.f1_score(y_test, y_pred, labels=None, pos_label=1, average=’binary’, sample_weight=None)\n";
			}
		}
		
		//LA STRATIFICATION
		String stratificationCode = "";
		if(stratification instanceof CrossValidation) {//c'est de la crossValidation
			CrossValidation cross = (CrossValidation) stratification;
			int numRep = cross.getNumber();
			//créer le x_train et le y_train
			
			
			stratificationCode += "skf = StratifiedKFold(n_splits="+numRep+", shuffle=True)\n";
			stratificationCode +="for index, (train_indices, val_indices) in enumerate(skf.split(X, Y)):\n";
			stratificationCode +="    print 'Training on fold ' + str(index+1) + '/10...'\n";
			stratificationCode +="    x_train, x_test = X[train_indices], X[val_indices]\n";
			stratificationCode +="    classifier.fit(x_train,y_train)\n";
			stratificationCode +="    y_train, y_test = Y[train_indices], Y[val_indices]\n";
			stratificationCode +="    y_pred = classifier.predict(x_test)\n";
			stratificationCode += metricCode+"\n";
			
			/////////////REVOIR CROSS VALIDATION 
		}else {//c'est du TrainingTest
			TrainingTest trainTest = (TrainingTest) stratification;
			int pourcentageTraining = trainTest.getNumber();
			stratificationCode += "x_train, x_test, y_train, y_test = train_test_split(X, Y, test_size="+pourcentageTraining+")\n";
			stratificationCode +="classifier.fit(x_train,y_train)\n";
			stratificationCode += "y_pred = classifier.predict(x_test)\n";
			stratificationCode += metricCode;
		}


		//variable predicitive par defaut est la derniere du tableau
		//FrameworkLang framework = mlAlgorithm.getFramework();
		pandasCode += "\nprint (mml_data)\n"; 
		pandasCode += codeFormulaY;
		pandasCode += codeFormulaX;
		pandasCode += algoML;
		pandasCode += stratificationCode;
		
		System.out.println(pandasCode);
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		// end of Python generation


		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}



	}

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}


}
