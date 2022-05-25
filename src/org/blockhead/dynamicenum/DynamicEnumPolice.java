package org.blockhead.dynamicenum;

import java.util.Map;

@FunctionalInterface
public interface DynamicEnumPolice {
	<E extends DynamicEnum<E>> void evaluate(Map<String, E> map, E[] elements);
}
