package com.yxedu.earth.utils.groovy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * {@link GroovyScriptCachingBuilder} 缓存了GroovyShell. \
 * 因为每一次解析Groovy脚本都会创建出一个新的类.为了避免内存问题, 我们需要缓存可用的Groovy脚本.
 */
public class GroovyScriptCachingBuilder {
  private GroovyShell shell = new GroovyShell();
  private Cache<String, Script> scripts = CacheBuilder.newBuilder()
      .initialCapacity(10)
      .maximumSize(100)
      .build();

  /**
   * 获取DSL脚本.根据expression进行进行了缓存处理, 避免类爆炸.
   */
  public Script getScript(final String expression) {
    Script script;
    if (scripts.getIfPresent(expression) != null) {
      script = scripts.getIfPresent(expression);
    } else {
      script = shell.parse(expression);
      scripts.put(expression, script);
    }
    return script;
  }
}
