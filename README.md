@@ -0,0 +1,89 @@
# Default

> Base
```
public class DefaultDynamicEnum extends DynamicEnum<DefaultDynamicEnum> {

	private DefaultDynamicEnum(String name, int ordinal) {
		super(name, ordinal);
	}

	public static DefaultDynamicEnum valueOf(String name) {
		return valueOf(DefaultDynamicEnum.class, name);
	}

	public static DefaultDynamicEnum[] values() {
		return values(DefaultDynamicEnum.class);
	}
}
```

> If static variable is set at load
```
private static DefaultDynamicEnum[] values = null;// loaded values

public static DefaultDynamicEnum valueOf(String name) {
  for(DefaultDynamicEnum value : values) {
    if(value.name().equals(name)) return value;
  }
  throw new IllegalArgumentException("No enum constant " + DefaultDynamicEnum.class.getCanonicalName() + "." + name);
}

public static DefaultDynamicEnum[] values() {
  return values;
}
```

# Private

> Base
```
public class PrivateDynamicEnum extends DynamicEnum<PrivateDynamicEnum> {
	private static final DynamicEnumLoader<PrivateDynamicEnum> loader = new DynamicEnumLoader<PrivateDynamicEnum>() {
		@Override
		public PrivateDynamicEnum[] load() {
			return null; // TODO load Enum
		}
	};
	
	private PrivateDynamicEnum(String name, int ordinal) {
		super(name, ordinal);
	}

	public static PrivateDynamicEnum[] load() {
		synchronized (PrivateDynamicEnum.class) {
			return load(PrivateDynamicEnum.class, loader);
		}
	}
	
	...
}
```

> new instance with reflection

```
public PrivateDynamicEnum newInstance(String name, int ordinal) throws Exception {
  Constructor<PrivateDynamicEnum> constructor = PrivateDynamicEnum.class.getDeclaredConstructor(String.class, int.class);
  constructor.setAccessible(true);
  PrivateDynamicEnum instance = constructor.newInstance(name, ordinal);
  constructor.setAccessible(false);

  return instance;
}
```

# Extending Enums
```
public class ExtendingDynamicEnum extends DynamicEnum<ExtendingDynamicEnum> {

	private Object obj;
	
	private ExtendingDynamicEnum(String name, int ordinal, Object obj) {
		super(name, ordinal);
		
		this.obj = obj;
	}

}
```
