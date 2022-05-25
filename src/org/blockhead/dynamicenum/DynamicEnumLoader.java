package org.blockhead.dynamicenum;

public interface DynamicEnumLoader<E extends DynamicEnum<E>> {
	E[] load();
	
	default DynamicEnumPolicy policy() {
		return DynamicEnumPolicy.STRICT;
	}
}
