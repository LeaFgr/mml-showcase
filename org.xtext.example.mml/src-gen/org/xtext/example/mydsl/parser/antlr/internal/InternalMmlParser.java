package org.xtext.example.mydsl.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.xtext.example.mydsl.services.MmlGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalMmlParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'input'"
    };
    public static final int RULE_ID=5;
    public static final int RULE_WS=9;
    public static final int RULE_STRING=4;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_SL_COMMENT=8;
    public static final int RULE_INT=6;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=7;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalMmlParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalMmlParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalMmlParser.tokenNames; }
    public String getGrammarFileName() { return "InternalMml.g"; }



     	private MmlGrammarAccess grammarAccess;

        public InternalMmlParser(TokenStream input, MmlGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "MMLModel";
       	}

       	@Override
       	protected MmlGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleMMLModel"
    // InternalMml.g:64:1: entryRuleMMLModel returns [EObject current=null] : iv_ruleMMLModel= ruleMMLModel EOF ;
    public final EObject entryRuleMMLModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMMLModel = null;


        try {
            // InternalMml.g:64:49: (iv_ruleMMLModel= ruleMMLModel EOF )
            // InternalMml.g:65:2: iv_ruleMMLModel= ruleMMLModel EOF
            {
             newCompositeNode(grammarAccess.getMMLModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMMLModel=ruleMMLModel();

            state._fsp--;

             current =iv_ruleMMLModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMMLModel"


    // $ANTLR start "ruleMMLModel"
    // InternalMml.g:71:1: ruleMMLModel returns [EObject current=null] : ( (lv_input_0_0= ruleDataInput ) ) ;
    public final EObject ruleMMLModel() throws RecognitionException {
        EObject current = null;

        EObject lv_input_0_0 = null;



        	enterRule();

        try {
            // InternalMml.g:77:2: ( ( (lv_input_0_0= ruleDataInput ) ) )
            // InternalMml.g:78:2: ( (lv_input_0_0= ruleDataInput ) )
            {
            // InternalMml.g:78:2: ( (lv_input_0_0= ruleDataInput ) )
            // InternalMml.g:79:3: (lv_input_0_0= ruleDataInput )
            {
            // InternalMml.g:79:3: (lv_input_0_0= ruleDataInput )
            // InternalMml.g:80:4: lv_input_0_0= ruleDataInput
            {

            				newCompositeNode(grammarAccess.getMMLModelAccess().getInputDataInputParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_input_0_0=ruleDataInput();

            state._fsp--;


            				if (current==null) {
            					current = createModelElementForParent(grammarAccess.getMMLModelRule());
            				}
            				set(
            					current,
            					"input",
            					lv_input_0_0,
            					"org.xtext.example.mydsl.Mml.DataInput");
            				afterParserOrEnumRuleCall();
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMMLModel"


    // $ANTLR start "entryRuleDataInput"
    // InternalMml.g:100:1: entryRuleDataInput returns [EObject current=null] : iv_ruleDataInput= ruleDataInput EOF ;
    public final EObject entryRuleDataInput() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataInput = null;


        try {
            // InternalMml.g:100:50: (iv_ruleDataInput= ruleDataInput EOF )
            // InternalMml.g:101:2: iv_ruleDataInput= ruleDataInput EOF
            {
             newCompositeNode(grammarAccess.getDataInputRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDataInput=ruleDataInput();

            state._fsp--;

             current =iv_ruleDataInput; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDataInput"


    // $ANTLR start "ruleDataInput"
    // InternalMml.g:107:1: ruleDataInput returns [EObject current=null] : (otherlv_0= 'input' ( (lv_filelocation_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleDataInput() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_filelocation_1_0=null;


        	enterRule();

        try {
            // InternalMml.g:113:2: ( (otherlv_0= 'input' ( (lv_filelocation_1_0= RULE_STRING ) ) ) )
            // InternalMml.g:114:2: (otherlv_0= 'input' ( (lv_filelocation_1_0= RULE_STRING ) ) )
            {
            // InternalMml.g:114:2: (otherlv_0= 'input' ( (lv_filelocation_1_0= RULE_STRING ) ) )
            // InternalMml.g:115:3: otherlv_0= 'input' ( (lv_filelocation_1_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,11,FOLLOW_3); 

            			newLeafNode(otherlv_0, grammarAccess.getDataInputAccess().getInputKeyword_0());
            		
            // InternalMml.g:119:3: ( (lv_filelocation_1_0= RULE_STRING ) )
            // InternalMml.g:120:4: (lv_filelocation_1_0= RULE_STRING )
            {
            // InternalMml.g:120:4: (lv_filelocation_1_0= RULE_STRING )
            // InternalMml.g:121:5: lv_filelocation_1_0= RULE_STRING
            {
            lv_filelocation_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_filelocation_1_0, grammarAccess.getDataInputAccess().getFilelocationSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDataInputRule());
            					}
            					setWithLastConsumed(
            						current,
            						"filelocation",
            						lv_filelocation_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDataInput"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000010L});

}