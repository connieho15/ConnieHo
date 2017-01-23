package cs445assign1;

	public class KeyVal<K, V>
	{
	    private K key;
	    private String name;
	    private int value;

	    public KeyVal(K key, int value, String name)
	    {
	        this.key = key;
	        this.value = value;
	        this.name = name;
	    }

	    public K getKey()
	    {
	        return this.key;
	    }

	    public int getValue()
	    {
	        return this.value;
	    }

	    public K setKey(K key)
	    {
	        return this.key = key;
	    }

	    public int setValue(int value)
	    {
	        return this.value = value;
	    }
	    
	    public String setName(String name)
	    {
	        return this.name = name;
	    }

	    public String getName()
	    {
	        return this.name;
	    }
	    
	}


