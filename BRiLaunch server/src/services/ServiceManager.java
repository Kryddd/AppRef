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
	private static List<Boolean> servicesClassesActivated;

	static {
		servicesClasses = new ArrayList<Class<? extends Service>>();
		servicesClassesActivated = new ArrayList<Boolean>();
	}

	/**
	 * Ajoute un service à la liste
	 * 
	 * @param serviceClass
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClasseInvalideException
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void addService(Class<?> serviceClass)
			throws InstantiationException, IllegalAccessException, ClasseInvalideException {
		// Vérification de la validité de la classe par introspection
		checkServiceValide(serviceClass);

		// Ajoute la classe
		servicesClasses.add((Class<? extends Service>) serviceClass);
		servicesClassesActivated.add(true); // La classe est activée par défaut
		System.out.println("Classe " + serviceClass.getName() + " correctement ajoutée");
	}

	public static synchronized Class<? extends Service> getService(int numService) throws NotActivatedException {
		if(servicesClassesActivated.get(numService - 1)) {
			return servicesClasses.get(numService - 1);
		}
		throw new NotActivatedException();
	}

	/**
	 * Donne la liste des services disponibles et leurs auteurs
	 * 
	 * @return Liste des services
	 */
	public static String servicesList() {
		String liste = "";
		synchronized (ServiceManager.class) {
			liste = "Services disponibles :##";
			
			if (servicesClasses.size() == 0) {
				return liste + "Aucun";
			}
			
			for(int i = 0; i < servicesClasses.size(); i++) {
				Class<? extends Service> s = servicesClasses.get(i);
				String etatClasse = (servicesClassesActivated.get(i))?"Activé":"Désactivé";
				liste += (i + 1) + " : " + s.getSimpleName() + " Etat : " + etatClasse + " (auteur : " + s.getPackage().getName() + ")##";
			}

		}

		return liste;
	}

	/**
	 * Donne le nom du package d'un service Le nom du package correspond à son
	 * auteur
	 * 
	 * @param numService
	 * @return Nom du package
	 */
	public static String getServicePackage(int numService) {
		return servicesClasses.get(numService - 1).getPackage().getName();
	}

	/**
	 * Supprime un service du ServiceManager
	 * 
	 * @param numService
	 */
	public static void removeService(int numService) {
		synchronized (ServiceManager.class) {
			System.out.println("Suppression du service " + servicesClasses.get(numService) + " de "
					+ getServicePackage(numService));
			servicesClasses.remove(numService - 1);
		}

	}

	/**
	 * Remplace un service de la liste
	 * 
	 * @param serviceClass
	 * @param numServiceRempl
	 * @throws ClasseInvalideException
	 */
	@SuppressWarnings("unchecked")
	public static void editService(Class<?> serviceClass, int numServiceRempl) throws ClasseInvalideException {
		synchronized (ServiceManager.class) {
			checkServiceValide(serviceClass);

			// Remplace le service
			servicesClasses.set(numServiceRempl - 1, (Class<? extends Service>) serviceClass);
			System.out.println("Classe " + serviceClass.getName() + " correctement remplacée");
		}
	}

	/**
	 * Vérification de la validité de la classe par introspection
	 * 
	 * @param serviceClass
	 *            Classe vérifiée
	 * @throws ClasseInvalideException
	 */
	private static void checkServiceValide(Class<?> serviceClass) throws ClasseInvalideException {

		if (Modifier.isAbstract(serviceClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : abstraite");
		}

		if (!Modifier.isPublic(serviceClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : non publique");
		}

		if (!Service.class.isAssignableFrom(serviceClass)) {
			throw new ClasseInvalideException("Classe invalide : n implemente pas Service");
		}

		Field[] fields = serviceClass.getDeclaredFields();
		boolean fieldExists = false;
		// Vérification d'un attribut Socket public final
		for (Field f : fields) {
			if (f.getType().equals(Socket.class)) {
				if (Modifier.isPrivate(f.getModifiers())) {
					if (Modifier.isFinal(f.getModifiers())) {
						fieldExists = true;
						break;
					}
				}
			}
		}

		if (!fieldExists) {
			throw new ClasseInvalideException("Classe invalide : Pas d'attribut socket valable");
		}

		// Vérifie le constructeur
		Constructor<?> constrClass;
		try {
			constrClass = serviceClass.getConstructor(Socket.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ClasseInvalideException("Classe invalide : pas de constructeur avec socket");
		}

		if (!Modifier.isPublic(constrClass.getModifiers())) {
			throw new ClasseInvalideException("Classe invalide : le constructeur n'est pas public");
		}
	}
	
	/**
	 * Donne le nom complet d'un service à partir de son num
	 * @param numService
	 * @return Nom du service
	 */
	public static String getServiceName(int numService) {
		return servicesClasses.get(numService - 1).getName();
	}

	/**
	 * Change l'état du service
	 * @param numService
	 */
	public static void changeState(int numService) {
		servicesClassesActivated.set(numService - 1, !servicesClassesActivated.get(numService - 1));
	}
}
