package objects;

public abstract class Model3D
{
  protected float m_nX, m_nY, m_nZ;
  protected float m_rX, m_rY, m_rZ;
  protected float m_sX=1, m_sY=1, m_sZ=1;
  
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
  
  public abstract void render3D();
}
