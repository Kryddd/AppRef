package services;

/**
 * Exception utilisée lorsqu'une classe ne correspond pas
 * à la norme BRi
 * @author couderc1
 * @version 1.0
 */
public class ClasseInvalideException extends Exception {

	public ClasseInvalideException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
