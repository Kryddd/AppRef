package services;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {
	
	private static List<Class<? extends Service>> servicesClasses;
	
	static {
		servicesClasses = new ArrayList<Class<? extends Service>>();
	}
	
	public static synchronized void addService(Class<? extends Service> serviceClass) throws InstantiationException, IllegalAccessException, ClasseInvalideException {
		// Vérification de la validité de la classe par introspection
		
		if(Modifier.isAbstract(serviceClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : abstraite");
		}
		
		if(!Modifier.isPublic(serviceClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : non publique");
		}
		
		Constructor<? extends Service> constrClass;
		try {
			constrClass = serviceClass.getConstructor(Socket.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ClasseInvalideException("Classe invalide : pas de constructeur avec socket");
		}
		
		if(!Modifier.isPublic(constrClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : le constructeur n'est pas public");
		}
		
		Field[] fields = serviceClass.getFields();
		boolean fieldExists = false;
		
		// Vérification d'un attribut Socket public final
		for(Field f : fields) {
			if(f.getType().equals(Socket.class)) {
				if(Modifier.isPublic(f.getModifiers())) {
					if(Modifier.isFinal(f.getModifiers())) {
						fieldExists = true;
						break;
					}
				}
			}
		}
		
		if(!fieldExists) {
			throw new ClasseInvalideException("Classe invalide : Pas d'attibut socket valable");
		}
		
		
		// Vérifie la methode toStringue
		try {
			Method methClass = serviceClass.getMethod("toStringue");
			
			if(!Modifier.isPublic(methClass.getModifiers())) {
				throw new ClasseInvalideException("Classe invalide : pas de methode toStringue accessible");
			}
			
			if(!methClass.getReturnType().equals(String.class)) {
				throw new ClasseInvalideException("Classe invalide : pas de methode toStringue qui renvoie un String");
			}
			
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ClasseInvalideException("Classe invalide : pas de methode toStringue valable");
		}
		
		servicesClasses.add(serviceClass);
		System.out.println("Classe " + serviceClass.getSimpleName() + " correctement ajoutée");
	}
	
	public static synchronized Class<? extends Service> getService(int numService) {
		return servicesClasses.get(numService - 1);
	}
	
	public static String servicesList() {
		String liste = "";
		synchronized(ServiceManager.class) {
			liste = "Services disponibles :\n";
			int i = 0;
			for(Class<?> c : servicesClasses) {
				liste = liste + (++i) + " : " + c.getSimpleName() + "##";
			}
		}
		
		return liste;
	}
}
