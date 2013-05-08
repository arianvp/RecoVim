package arian.recovim;

import java.awt.AWTException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;

import com.sun.speech.engine.recognition.BaseRecognizer;
import com.sun.speech.engine.recognition.BaseRuleGrammar;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.jsgf.JSGFGrammar;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class RecoVim implements Runnable {

    private final ScriptEngine engine;
    private final BaseRecognizer baseRecognizer;
    private final JSGFGrammar grammar;
    private final Recognizer recognizer;
    private final Microphone microphone;
    private final Robot robot;

    private boolean running = true;

    public RecoVim() throws AWTException {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        robot = new Robot();
        ConfigurationManager cm = new ConfigurationManager(
                RecoVim.class.getResource("config.xml"));
        recognizer = (Recognizer) cm.lookup("recognizer");
        grammar = (JSGFGrammar) cm.lookup("jsgfGrammar");
        microphone = (Microphone) cm.lookup("microphone");

        baseRecognizer = new BaseRecognizer(grammar.getGrammarManager());

        engine.put("robot", robot);
        engine.put("grammar", grammar);
    }

    public static void main(String[] args) throws AWTException {
        new RecoVim().run();
    }

    @Override
    public void run() {
        System.out.println("Loading recognizer ...");
        recognizer.allocate();
        System.out.println("Ready");

        if (microphone.startRecording()) {
            while (running) {
                Result result = recognizer.recognize();
                String bestResult = result.getBestFinalResultNoFiller();
                System.out.println(bestResult);
                RuleGrammar ruleGrammar = new BaseRuleGrammar(baseRecognizer,
                        grammar.getRuleGrammar());
                try {
                    RuleParse ruleParse = ruleGrammar.parse(bestResult, null);
                    if (ruleParse == null) {
                        continue;
                    }
                    switch (ruleParse.getRuleName().getRuleName()) {
                     // if number, parse those.
                    // TODO: THIS IS A HARDCODED HACK
                    }

                    parseTags(ruleParse);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Can't initialize microphone, halting!");
        }

    }

    private void parseTags(RuleParse ruleParse) throws ScriptException {
        if (ruleParse == null) {
            return;
        }
        for (String tag : ruleParse.getTags()) {
            engine.eval(tag);
        }
    }
}
