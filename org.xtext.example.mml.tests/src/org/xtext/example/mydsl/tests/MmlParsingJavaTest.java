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
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.LogisticRegression;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SVM;

import com.google.common.io.Files;
import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { pourcentageTraining 70 }\n"
				+ "recall\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void compileDataInput() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo2.csv\" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { pourcentageTraining 70 }\n"
				+ "recall\n"
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
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")";						
		String pandasCode = pythonImport + csvReading;
		
		
		MLChoiceAlgorithm mlAlgorithm = result.getAlgorithm();
		MLAlgorithm algo = mlAlgorithm.getAlgorithm();
		String algoML;
		
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
			algoML = "from sklearn.tree import DecisionTreeClassifier /n classifier = DecisionTreeClassifier(max_depth=" +max_depth+") /n classifier.fit(x_train,y_train)";
		}
		else if (algo instanceof LogisticRegression) {
			LogisticRegression lr = (LogisticRegression) algo;
		}
		else if(algo instanceof RandomForest) {
			RandomForest rf = (RandomForest) algo;
		}
		
		RFormula formula = result.getFormula();
		String codeFormulaY;
		String codeFormulaX;
		if(formula == null) { //si aucune formule n'a été donnée
			codeFormulaY = "Y = mml_data[-1]";
			codeFormulaX = "X = mml_data[:-1]";
		}else {
			
			FormulaItem y = formula.getPredictive();
			if(y == null) { //si la predictive n'a pas été renseignée
				codeFormulaY = "Y = mml_data[-1]";
			}else {
			if (y.getColName() != null) { //si colname est un string
				codeFormulaY = "Y = mml_data["+y+"] /n";
			}else { ////si colname est un int
				codeFormulaY = "Y = mml_data.iloc[:,"+y+"] /n";
			}
			
			}
			
			FormulaItem x = (FormulaItem) formula.getPredictors();
			if(x instanceof AllVariables) {
				codeFormulaX = "X = mml_data[:-1]";
			}
			if (x.getColName() != null) { //si colname est un string
				codeFormulaY = "Y = mml_data["+y+"] /n";
			}else { ////si colname est un int
				codeFormulaY = "Y = mml_data.iloc[:,"+y+"] /n";
			}
			

		}
		//variable predicitive par defaut est la derniere du tableau
		//FrameworkLang framework = mlAlgorithm.getFramework();
		
		pandasCode += "\nprint (mml_data)\n"; 

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
