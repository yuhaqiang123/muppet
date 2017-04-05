package cn.bronzeware.util.testframework;

import java.util.Map;

public interface TestUnitStorage {
	
	public Object store(Map<String, TestUnitMetaData>  testUnits);
	
	public Map<String, TestUnitMetaData> resolve();
}
