package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Par con dos elementos de tipo ObjectInputStream y ObjectOutputStream que se
 * corresponden con la infromación que nos piden almacenar de los usuarios
 * conectados al servidor
 * 
 * @author Javier Guzmán
 *
 */
public class InfoUsuarios {

	public InfoUsuarios(ObjectInputStream fin, ObjectOutputStream fout) {
		super();
		this.fin = fin;
		this.fout = fout;
	}

	private ObjectInputStream fin;
	private ObjectOutputStream fout;

	public ObjectInputStream getFin() {
		return fin;
	}

	public ObjectOutputStream getFout() {
		return fout;
	}

}
