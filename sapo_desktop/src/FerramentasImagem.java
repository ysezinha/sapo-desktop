import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.TransposeDescriptor;
import javax.media.jai.operator.TransposeType;
/*
 * FerramentasImagem.java
 *
 * Created on 3 de Julho de 2005, 15:36
 */

/**
 * @author Anderson Zanardi de Freitas
 * @author Edison Puig Maldonado
 */
public class FerramentasImagem extends LeJAIPanel{

	int X1,X2,Y1,Y2,W,H;
	public final int MIN_CROP_LENGTH = 50;
	
	KernelJAI[] kernels;
	KernelJAI kern_h, kern_v;
	static final String[] kernelLabels = { "Original",
		"Sobel",
		"Roberts",
		"Prewitt",
		"Frei-chen"
	};
    public boolean editou = false;
    public boolean executa;
    CropDialog cropDialog;
    public String op = "";
	private int br, ct;
    RenderedImage pimg = null;
    int numImg;
    
    public FerramentasImagem(SAPO obj) {
        super(obj);
    }
    
    public void inicializaParametros(){
    	copiaImagem();
        numImg = sapo.numImg;
        br = 0;
        ct = 0;
        executa = false;
        sapo.tbrImagem.resetComponents();
        sapo.tbrImagem.setEnabledAll(true);
        executa = true;
        editou = false;
        op = "";
    }
    
    private void copiaImagem() {
    	if ((sapo.jif[sapo.numImg] != null) && (sapo.jif[sapo.numImg].getRenderedImage() != null)) {
    		pimg = PlanarImage.wrapRenderedImage(sapo.jif[sapo.numImg].getRenderedImage());
    	}
    }
    
    public void contrasteUp() {
    	if (ct <= 15) {
    		ct += 5;
    		editou = true;
    		atualizarContraste();
    	}
    }
    
    public void contrasteDown() {
    	if (ct >= -15) {
    		ct -= 5;
    		editou = true;
    		atualizarContraste();
    	}
    }
    
    private void atualizarContraste() {
    	if (executa) {
    		executa = false;
    		sapo.tbrImagem.setEnabledAll(false);
    		sapo.tbrImagem.setEnabled("contraste", true);
    		op = "contraste";
    		atualizaContraste();
    	}
	}

    public void atualizaContraste() {
    	byte[] table = new byte[256];
    	for (int i = 0; i < 256; i++) {
    		double scale = i + (ct * Math.sin( (0.0245 * i) - 3.14));
    		if (scale > 255.0)
    			scale = 255.0;
    		else if (scale < 0.0)
    			scale = 0.0;
    		
    		table[i] = (byte) scale;
    	}
    	LookupTableJAI lookupTable = new LookupTableJAI(table);
    	ParameterBlock pb = new ParameterBlock();
    	pb.addSource(pimg);
    	pb.add(lookupTable);
    	try {
    		sapo.jif[sapo.numImg].setRenderedImage(JAI.create("lookup",pb));
    		sapo.showInternalFrameWithImage(sapo.numImg);
    		sapo.tbrImagem.resetComponents();
    		executa = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	sapo.release();
    }
    
	public void brilhoUp() {
        br += (br <= 245) ? 10 : 0;
        editou = true;
        atualizarBrilho();
    }
    
    public void brilhoDown() {
        br -= (br >= -245) ? 10 : 0;
        editou = true;
        atualizarBrilho();
    }
    
    public void atualizarBrilho() {
    	if (executa) {
    		executa = false;
    		sapo.tbrImagem.setEnabledAll(false);
    		sapo.tbrImagem.setEnabled("brilho", true);
    		op = "brilho";
    		atualizaBrilho();
    	}
    }
    
    public void atualizaBrilho() {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(pimg);
        double consts[] = {br, br, br};
        pb.add(consts);
        try {
        	sapo.jif[sapo.numImg].setRenderedImage(JAI.create("addconst", pb));
        	sapo.showInternalFrameWithImage(sapo.numImg);
        	sapo.tbrImagem.resetComponents();
        	executa = true;
        } catch (Exception e) {
        	 e.printStackTrace();
        }
        sapo.release();
    }
    
    public void transposeImg(final int tp){
    	if (executa) {
    		executa = false;
    		sapo.tbrImagem.setEnabledAll(false);
    		op = "transpose";
    		editou = true;
    		doTransposeImg(tp);
    	}
    }
    
    public void doTransposeImg(int tp){
        ParameterBlock pb = new ParameterBlock();
        TransposeType type;
        switch (tp){
            case 1: type = TransposeDescriptor.FLIP_HORIZONTAL; break;
            case 2: type = TransposeDescriptor.FLIP_VERTICAL; break;
            case 3: type = TransposeDescriptor.ROTATE_90; break;
            case 4: type = TransposeDescriptor.ROTATE_270; break;
            default: type = null;
        }
        pb.addSource(pimg);
        pb.add(type);
        try {
        	sapo.jif[sapo.numImg].setRenderedImage(JAI.create("transpose", pb));
        	sapo.showInternalFrameWithImage(sapo.numImg);
        	sapo.tbrImagem.resetComponents();
        	sapo.tbrImagem.setEnabledAll(false);
        	executa = true;
        } catch (Exception e) {
        	 e.printStackTrace();
        }
        sapo.release();
    }
    
    public void blurSharp(final int value){
    	if (executa) {
    		executa = false;
    		sapo.tbrImagem.setEnabledAll(false);
    		sapo.tbrImagem.setEnabled("blursharp", true);
    		op = "blursharp";
    		editou = true;
    		doBlurSharp(value);
    	}
    }
    
    private void doBlurSharp(final int value) {
    	ParameterBlock pb = new ParameterBlock();
		pb.addSource(pimg);
		float kData[] = new float[9];
		float alpha;
		if (value > 150) {
			alpha = (value-125.0f)/25.0f;
		} else {
			alpha = value/150.0f;
		}
		float beta = (1.0f-alpha)/8.0f;
		for (int i = 0; i < 9; i++) {
			kData[i] = beta;
		}
		kData[4] = alpha;
		
		KernelJAI k = new KernelJAI(3,3,1,1,kData);
		pb.add(k);
		try {
			sapo.jif[sapo.numImg].setRenderedImage(JAI.create("convolve", pb));
			sapo.showInternalFrameWithImage(sapo.numImg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executa = true;
			sapo.release();
		}
    }
    
    public void gradient(){
    	if (executa) {
    		executa = false;
    		sapo.tbrImagem.setEnabledAll(false);
    		sapo.tbrImagem.setEnabled("gradient", true);
    		op = "gradient";
    		editou = true;
    		doGradient();
    	}
    }
    
    public void doGradient() {
    	if ((kern_h == null) || (kern_v == null)) {
			initKernels();
		}
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(pimg);
		pb.add(kern_h);
		pb.add(kern_v);
		try {
			sapo.jif[sapo.numImg].setRenderedImage(JAI.create("invert", JAI.create("gradientmagnitude", pb)));
			sapo.showInternalFrameWithImage(sapo.numImg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executa = true;
			sapo.release();
		}
    }
    
    public void initKernels() {
        kernels = new KernelJAI[kernelLabels.length*2];

        float[] normal_h_data       = { 1.0F };
        float[] normal_v_data       = { 0.0F };
        
        float[] sobel_h_data        = { 1.0F,  0.0F, -1.0F,
                                        2.0F,  0.0F, -2.0F,
                                        1.0F,  0.0F, -1.0F
        };
        float[] sobel_v_data        = { -1.0F,  -2.0F, -1.0F,
                                         0.0F,   0.0F,  0.0F,
                                         1.0F,   2.0F,  1.0F
        };

        float[] roberts_h_data        = { 0.0F,  0.0F, -1.0F,
                                          0.0F,  1.0F,  0.0F,
                                          0.0F,  0.0F,  0.0F
        };
        float[] roberts_v_data        = { -1.0F,  0.0F, 0.0F,
                                           0.0F,  1.0F, 0.0F,
                                           0.0F,  0.0F, 0.0F
        };

        float[] prewitt_h_data        = { 1.0F,  0.0F, -1.0F,
                                          1.0F,  0.0F, -1.0F,
                                          1.0F,  0.0F, -1.0F
        };
        float[] prewitt_v_data        = { -1.0F, -1.0F, -1.0F,
                                           0.0F,  0.0F,  0.0F,
                                           1.0F,  1.0F,  1.0F
        };

        float[] freichen_h_data        = { 1.0F,   0.0F, -1.0F,
                                           1.414F, 0.0F, -1.414F,
                                           1.0F,   0.0F, -1.0F
        };
        float[] freichen_v_data        = { -1.0F,  -1.414F, -1.0F,
                                            0.0F,   0.0F,    0.0F,
                                            1.0F,   1.414F,  1.0F
        };

        kernels[0] = new KernelJAI(1, 1, normal_h_data);
        kernels[1] = new KernelJAI(1, 1, normal_v_data);
        kernels[2] = new KernelJAI(3, 3, sobel_h_data);
        kernels[3] = new KernelJAI(3, 3, sobel_v_data);
        kernels[4] = new KernelJAI(3, 3, roberts_h_data);
        kernels[5] = new KernelJAI(3, 3, roberts_v_data);
        kernels[6] = new KernelJAI(3, 3, prewitt_h_data);
        kernels[7] = new KernelJAI(3, 3, prewitt_v_data);
        kernels[8] = new KernelJAI(3, 3, freichen_h_data);
        kernels[9] = new KernelJAI(3, 3, freichen_v_data);
        kern_h = kernels[0];
        kern_v = kernels[1];
    }

    public void mousePressed(MouseEvent e) {
        X1=e.getX();
        Y1=e.getY();
        if ((op.equals("crop")) && (cropDialog != null)) {
        	if (!cropDialog.isVisible()) cropDialog.setVisible(true);
        	cropDialog.jtfLeft.setText(String.valueOf(X1));
        	cropDialog.jtfTop.setText(String.valueOf(Y1));
        	cropDialog.jtfRight.setText("");
        	cropDialog.jtfBottom.setText("");
        }
        sapo.repaint();
    } //mousePressed
   
    public void mouseDragged(MouseEvent e) {
        X2=e.getX();
        Y2=e.getY();
        if ((op.equals("crop")) && (cropDialog != null)) {
        	cropDialog.jtfRight.setText(String.valueOf(X2));
        	cropDialog.jtfBottom.setText(String.valueOf(Y2));
        }
        W=Math.abs(X2-X1);
        H=Math.abs(Y2-Y1);
        if (sapo.desenhoTipo == 1) {
            sapo.jif[sapo.numImg].jaiP.setRect(X1,Y1,W,H,java.awt.Color.GREEN, 1);
        }
    } //mouseDragged
    
    public void mouseReleased(MouseEvent e) {
    	int deltaX = X2 - X1;
    	int deltaY = Y2 - Y1;
    	if ((deltaX < MIN_CROP_LENGTH) || (deltaY < MIN_CROP_LENGTH))
    		if ((op.equals("crop")) && (cropDialog != null)) {
    			cropDialog.jtfLeft.setText("");
            	cropDialog.jtfTop.setText("");
            	cropDialog.jtfRight.setText("");
            	cropDialog.jtfBottom.setText("");
            }
    }
    
    public void desacoplar(int num) {
        if (cropDialog != null) {
    		cropDialog.setVisible(false);
    		cropDialog.dispose();
    	}
        executa = false;
        sapo.tbrImagem.setEnabledAll(true);
        sapo.tbrImagem.resetComponents();
        executa = true;
        super.desacoplar(num);
    }
    
    public void crop() {
    	if (executa) {
    		executa = false;
    		sapo.tbrImagem.setEnabledAll(false);
    		sapo.tbrImagem.setEnabled("crop", true);
    		op = "crop";
    		
    		sapo.desacoplar(sapo.numImg);
    		sapo.frmImagem.acoplar(sapo.numImg);
    		sapo.desenhoTipo = 1; // 1 = Retangulo
    		
    		cropDialog = new CropDialog(sapo);
    		cropDialog.pack();
    		SAPO.center(cropDialog);
    	}
    }
    
    public void doCrop(int x, int y, int width, int height) {
    	float zoom = sapo.jif[sapo.numImg].zoom / 100.0F;
    	ParameterBlock pb1 = new ParameterBlock();
    	ParameterBlock pb2 = new ParameterBlock();
    	pb1.addSource(pimg);  
    	pb1.add(x/zoom);  
    	pb1.add(y/zoom);  
    	pb1.add(width/zoom);  
    	pb1.add(height/zoom);
    	try {
    		sapo.jif[sapo.numImg].setRenderedImage(JAI.create("crop",pb1,null));
			pb2 = new ParameterBlock();  
			pb2.addSource(sapo.jif[sapo.numImg].getRenderedImage());
			pb2.add(-x/zoom);  
			pb2.add(-y/zoom);  
			sapo.jif[sapo.numImg].setRenderedImage(JAI.create("translate",pb2,null)); 
			sapo.showInternalFrameWithImage(sapo.numImg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executa = true;
			sapo.release();
		}
    }
}
