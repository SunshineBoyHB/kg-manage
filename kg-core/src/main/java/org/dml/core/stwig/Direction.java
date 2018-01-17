package org.dml.core.stwig;

/**
 * STwig根节点到STwig叶节点的方向枚举类
 * 
 * @author HuangBo
 *
 */
public enum Direction {

	/**
	 * 正向:root-->leaf
	 */
	STWIG_EDGE_FORWARD,

	/**
	 * 反向:root<--leaf
	 */
	STWIG_EDGE_REVERSE;
}
