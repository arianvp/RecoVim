package arian.recovim;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.speech.recognition.RuleParse;

public class ScriptTagsParser {
    private final ScriptEngine engine;

    public ScriptTagsParser() {

        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("JavaScript");

    }

    public void parseTags(RuleParse ruleParse) {
        if (ruleParse == null) {   
            return;
        }
        
        for(String s: ruleParse.getTags()) {
            try {
                engine.eval(s);
            } catch (ScriptException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
