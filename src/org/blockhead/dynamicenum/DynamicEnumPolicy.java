package org.blockhead.dynamicenum;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DynamicEnumPolicy {
	/**
     * @throws IllegalArgumentException if duplicate in key or value. or missing ordinal
	 */
	STRICT(new DynamicEnumPolice() {
		@Override
		public <E extends DynamicEnum<E>> void evaluate(Map<String, E> map, E[] elements) {
			List<E> list = Arrays.stream(elements)
				.filter(distinctBy("name", DynamicEnum::name))
				.filter(distinctBy("ordinal", DynamicEnum::ordinal))
				.sorted()
				.toList();
			
			int i = 0;
			for(E e : list) {
				if(e.ordinal() != i) throw new IllegalArgumentException("missing ordinal " + i);
				i++;
			}

			for(E e : list) {
				map.put(e.name(), e);
			}
		}
		
		private <T> Predicate<T> distinctBy(String name, Function<? super T, Object> keyExtractor) {
			Set<Object> set = ConcurrentHashMap.newKeySet();
			return t -> {
				Object key = keyExtractor.apply(t);
				if(set.add(key)) return true;
				throw new IllegalArgumentException("duplicate " + name + " - " + key);
			};
		}
	}),
	
	/**
	 * - In case of duplicate key, only the first one is allowed.<br>
	 * - Automatically sets the smallest integer in case of duplicate ordinal.<br>
	 */
	SMART(new DynamicEnumPolice() {
		@Override
		public <E extends DynamicEnum<E>> void evaluate(Map<String, E> map, E[] elements) {
			List<Integer> ordinals = new LinkedList<>();
			for(E e : elements) {
				String key = e.name();
				if(map.containsKey(key)) return;
				
				if(ordinals.contains(e.ordinal())) {
					int smallest = findMissingPositive(ordinals);
					forceChangeOrdinal(e, smallest);
				}
				ordinals.add(e.ordinal());
				
				map.put(key, e);
			}
		}
		
		public int findMissingPositive(List<Integer> nums) {
			Collections.sort(nums);
			
			int i = 0;
			while(nums.contains(i)) i++;
	        return i;
	    }
		
		public void forceChangeOrdinal(DynamicEnum<?> element, int ordinal) {
			try {
				Field field = element.getDeclaringClass().getDeclaredField("ordinal");
				field.setAccessible(true);
				field.setInt(element, ordinal);
				field.setAccessible(false);
			} catch (Exception e1) {}
		}
	}),
	
	/**
	 * just call {@link Map#put(Object, Object)}
	 */
	LENIENT(new DynamicEnumPolice() {
		@Override
		public <E extends DynamicEnum<E>> void evaluate(Map<String, E> map, E[] elements) {
			for(E e : elements) {
				map.put(e.name(), e);
			}
		}
	});
	
	private final DynamicEnumPolice police;
	
	private DynamicEnumPolicy(DynamicEnumPolice police) {
		this.police = police;
	}
	
	public <E extends DynamicEnum<E>> void evaluate(Map<String, E> map, E[] elements) {
		police.evaluate(map, elements);
	}
	
	@FunctionalInterface
	public interface DynamicEnumPolice {
		<E extends DynamicEnum<E>> void evaluate(Map<String, E> map, E[] elements);
	}
}
