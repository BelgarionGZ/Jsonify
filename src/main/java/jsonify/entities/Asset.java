package jsonify.entities;

import java.util.Map;

public class Asset {
	private String assetid;
	private String url;
	private String titulo;
	private Map<String, String> categorias = null;
	private String contenido;
	
	public String getAssetid() {
		return assetid;
	}
	
	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Map<String, String> getCategorias() {
		return categorias;
	}
	
	public void setCategorias(Map<String, String> categorias) {
		this.categorias = categorias;
	}
	
	public String getContenido() {
		return contenido;
	}
	
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
}