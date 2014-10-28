package snowmada.main.bean;

public class ProfileDealsBean{

	public String dealsId;
	public String dealsName;
	public String dealsImage;
	
	public ProfileDealsBean(String dealsId,String dealsName,String dealsImage){
		this.dealsId = dealsId;
		this.dealsName = dealsName;
		this.dealsImage = dealsImage;
	}

	public String getDealsId()
	{
		return dealsId;
	}

	public void setDealsId(String dealsId)
	{
		this.dealsId = dealsId;
	}

	public String getDealsName()
	{
		return dealsName;
	}

	public void setDealsName(String dealsName)
	{
		this.dealsName = dealsName;
	}

	public String getDealsImage()
	{
		return dealsImage;
	}

	public void setDealsImage(String dealsImage)
	{
		this.dealsImage = dealsImage;
	}
}
