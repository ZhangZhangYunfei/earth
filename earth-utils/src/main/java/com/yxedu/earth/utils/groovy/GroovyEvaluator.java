package com.yxedu.earth.utils.groovy;

import com.google.common.collect.Maps;

import groovy.lang.Binding;
import groovy.lang.Script;

import java.util.Map;

/**
 * {@link GroovyEvaluator} 封装了对应于Groovy DSL的解析和运行的逻辑.
 */
public class GroovyEvaluator {
  private static GroovyScriptCachingBuilder groovyScriptCachingBuilder = new
      GroovyScriptCachingBuilder();
  private Map<String, Object> variables = Maps.newHashMap();

  public GroovyEvaluator(final Map<String, Object> contextVariables) {
    variables.putAll(contextVariables);
  }

  public void setVariables(final Map<String, Object> answers) {
    variables.putAll(answers);
  }

  public void setVariable(final String name, final Object value) {
    variables.put(name, value);
  }

  /**
   * 执行DSL表达式.
   */
  public Object evaluateExpression(String expression) {
    final Binding binding = new Binding(variables);
    Script script = groovyScriptCachingBuilder.getScript(expression);
    script.setBinding(binding);
    return script.run();
  }
}
