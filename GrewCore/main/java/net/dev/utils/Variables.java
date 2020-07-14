package net.dev.utils;

import net.dev.utils.string.*;

import java.util.*;
import java.util.regex.*;

public class Variables {
    private Pattern pattern;
    private String text;
    private String textOrigin;
    private List<Variable> variableList = new ArrayList<>();

    public Variables(String text) {
        this(text, "<([^<>]+)>");
    }

    public Variables(String text, String regex) {
        this(text, Pattern.compile(regex));
    }

    public Variables(String text, Pattern pattern) {
        this.text = text;
        this.textOrigin = text;
        this.pattern = pattern;
    }

    public Variables reset() {
        text = textOrigin;
        variableList.clear();
        return this;
    }

    public Variables find() {
        reset();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String group = matcher.group();
            String[] textOther = text.split(group);
            String textLeft = text.indexOf(group) > 0 ? textOther[0] : null;
            String textRight = textOther.length >= 2 ? text.substring(text.indexOf(group) + group.length()) : null;
            if (textLeft != null) {
                variableList.add(new Variable(textLeft, false));
            }
            variableList.add(new Variable(group.substring(1, group.length() - 1), true));
            if (textRight != null && !pattern.matcher(textRight).find()) {
                variableList.add(new Variable(textRight, false));
            } else {
                text = String.valueOf(textRight);
            }
        }
        if (variableList.size() == 0) {
            variableList.add(new Variable(text, false));
        }
        return this;
    }

    @Override
    public String toString() {
        return Strings.replaceWithOrder("VariableFormatter'{'pattern={0}, text=''{1}'', textOrigin=''{2}'', variableList={3}'}'", pattern, text, textOrigin, variableList);
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public String getText() {
        return text;
    }

    public List<Variable> getVariableList() {
        return variableList;
    }

    // *********************************
    //
    //         Public classes
    //
    // *********************************

    public static class Variable {

        private final String text;
        private final boolean variable;

        public Variable(String text, boolean variable) {
            this.text = text;
            this.variable = variable;
        }

        public String getText() {
            return text;
        }

        public boolean isVariable() {
            return variable;
        }

        @Override
        public String toString() {
            return Strings.replaceWithOrder("Variable'{'text=''{0}'', variable={1}'}'", text, variable);
        }
    }
}
