package objects;

public abstract class Model3D
{
  public float deltax, deltay, deltaz;
  public float m_nX, m_nY, m_nZ;
  public float m_rX, m_rY, m_rZ;
  public float m_sX=1, m_sY=1, m_sZ=1;
  public float WIDTH;
  public boolean show=true; //Options for 3D rendering, if false it wont show
  
  public Model3D(float w){
	  this.WIDTH = w;	  
  }
  public Model3D(){
	  
  }
  public void setPosition(float p_X, float p_Y, float p_Z)
  {
    m_nX=p_X; m_nY=p_Y; m_nZ=p_Z;
  }
  public void setRotation(float p_X, float p_Y, float p_Z)
  {
    m_rX=p_X; m_rY=p_Y; m_rZ=p_Z;
  }
  public void setScaling(float p_X, float p_Y, float p_Z)
  {
    m_sX=p_X; m_sY=p_Y; m_sZ=p_Z;
  }
  public String toString(){	//toString return current position of model
	  return "("+m_nX+","+m_nY+","+m_nZ+")";
  }
  public abstract void render3D();
}
