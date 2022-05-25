# Default

> Basic (required)
```
public class BasicDynamicEnum extends DynamicEnum<BasicDynamicEnum> {

	private BasicDynamicEnum(String name, int ordinal) {
		super(name, ordinal);
	}

	public static BasicDynamicEnum valueOf(String name) {
		return valueOf(BasicDynamicEnum.class, name);
	}

	public static BasicDynamicEnum[] values() {
		return values(BasicDynamicEnum.class);
	}
}
```

> If static variable is set at load
```
private static BasicDynamicEnum[] values = null;// loaded values

public static BasicDynamicEnum valueOf(String name) {
  for(BasicDynamicEnum value : values) {
    if(value.name().equals(name)) return value;
  }
  throw new IllegalArgumentException("No enum constant " + BasicDynamicEnum.class.getCanonicalName() + "." + name);
}

public static BasicDynamicEnum[] values() {
  return values;
}
```

## Private

> example
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

## Extending Enums
```
public class ExtendingDynamicEnum extends DynamicEnum<ExtendingDynamicEnum> {

	private Object obj;
	
	private ExtendingDynamicEnum(String name, int ordinal, Object obj) {
		super(name, ordinal);
		
		this.obj = obj;
	}

}
```
