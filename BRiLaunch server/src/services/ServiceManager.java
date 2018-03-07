package services;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {
	
	private static List<Class<?>> servicesClasses;
	
	static {
		servicesClasses = new ArrayList<Class<?>>();
	}
	
	public static void addService(Class<?> ServiceClass) throws InstantiationException, IllegalAccessException {
		// TODO introspection et throw exception en cas de non conformité
		
		servicesClasses.add(ServiceClass);
	}
	
	public static Service getService(int numService) throws InstantiationException, IllegalAccessException {
		// TODO peut etre passer des parametres
		return (Service) servicesClasses.get(numService).newInstance();
	}
	
	public static String servicesList() {
		String liste = "";
		int i = 0;
		synchronized(ServiceManager.class) {
			for(Class<?> c : servicesClasses) {
				liste = liste + (++i) + " : " + c.getSimpleName() + "\n";
			}
		}
		
		return liste;
	}
}
