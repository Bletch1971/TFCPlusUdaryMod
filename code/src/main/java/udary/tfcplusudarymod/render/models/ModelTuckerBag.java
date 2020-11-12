package udary.tfcplusudarymod.render.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTuckerBag extends ModelBiped
{
	protected ModelRenderer bagMain;
	protected ModelRenderer bagMainFull;

    public ModelTuckerBag() 
    {
        this.textureWidth = 65;
        this.textureHeight = 40;

        createBag();
        createBagFull();
    }
    
    protected void createBag()
    {
    	bagMain = new ModelRenderer(this, 1, 10);
    	bagMain.addBox(-3.5F, 0F, 2F, 7, 8, 3, 0.0F);
        setRotation(bagMain, 0F, 0F, 0F);
        
        ModelRenderer bagTop = new ModelRenderer(this, 3, 4);
        bagTop.addBox(-3F, -0.5F, 2F, 6, 1, 2, 0.0F);
        setRotation(bagTop, 0F, 0F, 0F);
       
        ModelRenderer stringBackLeft = new ModelRenderer(this, 22, 13);
        stringBackLeft.addBox(2.5F, -0.1F, -2.1F, 1, 8, 0, 0.0F);
        setRotation(stringBackLeft, 0F, 0F, 0F);
        
        ModelRenderer stringBackRight = new ModelRenderer(this, 22, 13);
        stringBackRight.addBox(-3.5F, -0.10F, -2.1F, 1, 8, 0, 0.0F);
        setRotation(stringBackRight, 0F, 0F, 0F);
        
        bagMain.addChild(bagTop);
        bagMain.addChild(stringBackLeft);
        bagMain.addChild(stringBackRight);
    }
    
    protected void createBagFull()
    {
    	bagMainFull = new ModelRenderer(this, 1, 10);
    	bagMainFull.addBox(-3.5F, 2.0F, 4F, 7, 8, 3, 2.0F);
        setRotation(bagMainFull, 0F, 0F, 0F);
        
        ModelRenderer bagTop = new ModelRenderer(this, 3, 4);
        bagTop.addBox(-3F, 1.5F, 4F, 6, 1, 2, 2.0F);
        setRotation(bagTop, 0F, 0F, 0F);
       
        ModelRenderer stringBackLeft = new ModelRenderer(this, 22, 13);
        stringBackLeft.addBox(2.5F, -0.1F, -2.1F, 1, 8, 0, 0.0F);
        setRotation(stringBackLeft, 0F, 0F, 0F);
        
        ModelRenderer stringBackRight = new ModelRenderer(this, 22, 13);
        stringBackRight.addBox(-3.5F, -0.10F, -2.1F, 1, 8, 0, 0.0F);
        setRotation(stringBackRight, 0F, 0F, 0F);
        
        bagMainFull.addChild(bagTop);
        bagMainFull.addChild(stringBackLeft);
        bagMainFull.addChild(stringBackRight);
    }
    
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        
        bagMain.render(f5);
    }

    public void renderFull(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
        bagMainFull.render(f5);
    }

    protected void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        if (entity != null && entity.isSneaking()) 
        {
        	bagMain.rotateAngleX = 0.5F;
        	bagMainFull.rotateAngleX = 0.5F;
        } 
        else 
        {
        	bagMain.rotateAngleX = 0.0F;
        	bagMainFull.rotateAngleX = 0.0F;
        }
    }
}
