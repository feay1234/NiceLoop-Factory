package database;

public class Image {
     
	//private variables
    private String _name;
    private String _comment;
    private String _upload;
    
    public Image(){
    	
    }
    
    public Image(String _name) {
		super();
		this._name = _name;
	}
    
    public Image(String _name, String _comment, String _upload){
    	this._name = _name;
    	this._comment = _comment;
    	this._upload = _upload;
    }
    
	public String get_name() {
		return _name;
	}
	
	public void set_name(String _name) {
		this._name = _name;
	}
	
	public String get_comment() {
		return _comment;
	}
	
	public void set_comment(String _comment) {
		this._comment = _comment;
	}

	public String get_upload() {
		return _upload;
	}

	public void set_upload(String _upload) {
		this._upload = _upload;
	}
     
}