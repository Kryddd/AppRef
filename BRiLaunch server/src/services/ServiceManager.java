package services;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui contient les services accessibles sur BRiLaunch
 * 
 * @author couderc1
 * @version 1.0
 */
public class ServiceManager {
	
	private static List<Class<? extends Service>> servicesClasses;
	
	static {
		servicesClasses = new ArrayList<Class<? extends Service>>();
	}
	
	/**
	 * Ajoute un service � la liste
	 * @param serviceClass
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClasseInvalideException
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void addService(Class<?> serviceClass) throws InstantiationException, IllegalAccessException, ClasseInvalideException {
		// V�rification de la validit� de la classe par introspection
		
		if(Modifier.isAbstract(serviceClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : abstraite");
		}
		
		if(!Modifier.isPublic(serviceClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : non publique");
		}
		
		if(!Service.class.isAssignableFrom(serviceClass)) {
			throw new ClasseInvalideException("Classe invalide : n implemente pas Service");
		}
		
		Field[] fields = serviceClass.getDeclaredFields();
		boolean fieldExists = false;
		// V�rification d'un attribut Socket public final
		for(Field f : fields) {
			if(f.getType().equals(Socket.class)) {
				if(Modifier.isPrivate(f.getModifiers())) {
					if(Modifier.isFinal(f.getModifiers())) {
						fieldExists = true;
						break;
					}
				}
			}
		}
		
		if(!fieldExists) {
			throw new ClasseInvalideException("Classe invalide : Pas d'attribut socket valable");
		}
		
		// V�rifie le constructeur
		Constructor<?> constrClass;
		try {
			constrClass = serviceClass.getConstructor(Socket.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ClasseInvalideException("Classe invalide : pas de constructeur avec socket");
		}
		
		if(!Modifier.isPublic(constrClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : le constructeur n'est pas public");
		}
		
		// Ajoute la classe
		servicesClasses.add((Class<? extends Service>) serviceClass);
		System.out.println("Classe " + serviceClass.getSimpleName() + " correctement ajout�e");
	}
	
	public static synchronized Class<? extends Service> getService(int numService) {
		return servicesClasses.get(numService - 1);
	}
	
	/**
	 * Donne la liste des services disponibles et leurs auteurs
	 * @return Liste des services
	 */
	public static String servicesList() {
		String liste = "";
		synchronized(ServiceManager.class) {
			liste = "Services disponibles :##";
			int i = 0;
			if(servicesClasses.size() == 0) {
				return liste + "Aucun";
			}
			for(Class<?> c : servicesClasses) {
				liste = liste + (++i) + " : " + c.getSimpleName() + " (auteur : " + c.getPackage().getName() + ")##";
			}
			
		}
		
		return liste;
	}
	
	/**
	 * Donne le nom du package d'un service
	 * Le nom du package correspond � son auteur
	 * @param numService
	 * @return Nom du package
	 */
	public static String getServicePackage(int numService) {
		return servicesClasses.get(numService - 1).getPackage().getName();
	}

	/**
	 * Supprime un service du ServiceManager
	 * @param numService
	 */
	public static void removeService(int numService) {
		synchronized(ServiceManager.class) {
			System.out.println("Suppression du service " + servicesClasses.get(numService) 
			+ " de " + getServicePackage(numService));
			servicesClasses.remove(numService - 1);
		}
		
	}
}
