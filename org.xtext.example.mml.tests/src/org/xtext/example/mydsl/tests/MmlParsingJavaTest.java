package org.xtext.example.mydsl.tests;

import java.io.File;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MMLModel;

import com.google.common.io.Files;
import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("input \"foo.csv\"");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void compileDataInput() throws Exception {
		MMLModel result = parseHelper.parse("input \"foo.csv\"");
		String fileLocation = result.getInput().getFilelocation();
	
		
		String pythonImport = "import pandas as pd\n"; 
		String COLUMN_SEPARATOR = ","; // by default
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(COLUMN_SEPARATOR) + ")";						
		String pandasCode = pythonImport + csvReading;
		
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		
		
	}

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}


}
