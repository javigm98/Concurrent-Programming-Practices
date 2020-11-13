package parte2;

import java.io.Serializable;

/**
 * Clase abstracta de la que heredan todos los tipos de mensaje del sistema
 * Contiene como atributos el tipo de mensaje y un string con el identificador
 * del origen y el destino. Como nuestros mensajes siempren irán de un cliente
 * al servidor o viceversa, origenId o destId siempre serán el nombre del
 * servidor
 * 
 * @author Javier Guzmán
 *
 */
public abstract class Mensaje implements Serializable {
	private TipoMensaje tipo;
	private String origenId;
	private String destId;

	public Mensaje(TipoMensaje tipo, String origenId, String destId) {
		super();
		this.tipo = tipo;
		this.origenId = origenId;
		this.destId = destId;
	}

	public TipoMensaje getTipo() {
		return tipo;
	}

	public String getOrigenId() {
		return origenId;
	}

	public String getDestId() {
		return destId;
	}

}
