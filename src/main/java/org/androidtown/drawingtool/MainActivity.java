package org.androidtown.drawingtool;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;

public class MainActivity extends Activity implements OnClickListener{

    private static final int PICK_FROM_GALLERY = 2;
    private ImageView imgview;
    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn,newBtn,saveBtn,effectBtn, cubeBtn, highlighterBtn, blurBtn, cameraBtn,galleryBtn,paint_brown,paint_red,paint_orange, paint_yellow, paint_green, paint_teal, paint_dodgerBlue, paint_mediumPurple, paint_pink, paint_white,paint_darkGray,paint_black;
    private Button imageFilterBtn, penBtn;
    private float smallBrush, mediumBrush, largeBrush;
    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window win = getWindow();
        win.requestFeature(Window.FEATURE_NO_TITLE);
        win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        // setContentView(R.layout.activity_camera_gallery);

        drawView = (DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

        glView = new GLSurfaceView(this);
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glView.setRenderer(new MyGLRenderer(this));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        paint_brown = (ImageButton)findViewById(R.id.paint_brown); //(NEW)
        paint_red = (ImageButton)findViewById(R.id.paint_red);
        paint_orange = (ImageButton)findViewById(R.id.paint_orange);
        paint_yellow = (ImageButton)findViewById(R.id.paint_yellow);
        paint_green = (ImageButton)findViewById(R.id.paint_green);
        paint_teal = (ImageButton)findViewById(R.id.paint_teal);
        paint_dodgerBlue = (ImageButton)findViewById(R.id.paint_dodgerBlue);
        paint_mediumPurple = (ImageButton)findViewById(R.id.paint_mediumPurple);
        paint_pink = (ImageButton)findViewById(R.id.paint_pink);
        paint_white = (ImageButton)findViewById(R.id.paint_white);
        paint_darkGray = (ImageButton)findViewById(R.id.paint_darkGray);
        paint_black = (ImageButton)findViewById(R.id.paint_black);

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(mediumBrush);

        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        galleryBtn = (ImageButton) findViewById(R.id.btn_select_gallery);
        galleryBtn.setOnClickListener(this);

        effectBtn = (ImageButton) findViewById(R.id.effect_btn);
        effectBtn.setOnClickListener(this);

        cubeBtn = (ImageButton) findViewById(R.id.cube_btn);
        cubeBtn.setOnClickListener(this);

        highlighterBtn = (ImageButton) findViewById(R.id.highlighter_brush);
        highlighterBtn.setOnClickListener(this);

        blurBtn = (ImageButton) findViewById(R.id.blur_brush);
        blurBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.btn_select_gallery){
            try {
                Intent gintent = new Intent();
                gintent.setType("image/*");
                gintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(gintent, "Select Picture"),
                        PICK_FROM_GALLERY);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
//respond to clicks

        if (view.getId() == R.id.draw_btn) {
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush Size & Color");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            final ImageButton brownBtn = (ImageButton) brushDialog.findViewById(R.id.paint_brown);
            brownBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = (ImageButton) brownBtn;
                    String color = brownBtn.getTag().toString();
                    drawView.setColor(color);
                    //imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = brownBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton redBtn = (ImageButton) brushDialog.findViewById(R.id.paint_red);
            redBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = redBtn;
                    String color = redBtn.getTag().toString();
                    drawView.setColor(color);
                    //imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = redBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton orangeBtn = (ImageButton) brushDialog.findViewById(R.id.paint_orange);
            orangeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = orangeBtn;
                    String color = orangeBtn.getTag().toString();
                    drawView.setColor(color);
                    // imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    // currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = orangeBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton yellowBtn = (ImageButton) brushDialog.findViewById(R.id.paint_yellow);
            yellowBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = yellowBtn;
                    String color = yellowBtn.getTag().toString();
                    drawView.setColor(color);
                    //   imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //  currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = yellowBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton greenBtn = (ImageButton) brushDialog.findViewById(R.id.paint_green);
            greenBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = greenBtn;
                    String color = greenBtn.getTag().toString();
                    drawView.setColor(color);
                    //     imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //   currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = greenBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton tealBtn = (ImageButton) brushDialog.findViewById(R.id.paint_teal);
            tealBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = tealBtn;
                    String color = tealBtn.getTag().toString();
                    drawView.setColor(color);
                    //     imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //   currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = tealBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton dodgerBlueBtn = (ImageButton) brushDialog.findViewById(R.id.paint_dodgerBlue);
            dodgerBlueBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = dodgerBlueBtn;
                    String color = dodgerBlueBtn.getTag().toString();
                    drawView.setColor(color);
                    //       imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //     currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = dodgerBlueBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton mediumPurpleBtn = (ImageButton) brushDialog.findViewById(R.id.paint_mediumPurple);
            mediumPurpleBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = mediumPurpleBtn;
                    String color = mediumPurpleBtn.getTag().toString();
                    drawView.setColor(color);
                    //         imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //       currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = mediumPurpleBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton pinkBtn = (ImageButton) brushDialog.findViewById(R.id.paint_pink);
            pinkBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = pinkBtn;
                    String color = pinkBtn.getTag().toString();
                    drawView.setColor(color);
                    //        imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //      currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = pinkBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton whiteBtn = (ImageButton) brushDialog.findViewById(R.id.paint_white);
            whiteBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = whiteBtn;
                    String color = whiteBtn.getTag().toString();
                    drawView.setColor(color);
                    //       imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //     currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = whiteBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton darkGrayBtn = (ImageButton) brushDialog.findViewById(R.id.paint_darkGray);
            darkGrayBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = darkGrayBtn;
                    String color = darkGrayBtn.getTag().toString();
                    drawView.setColor(color);
                    //       imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //     currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = darkGrayBtn;
                    brushDialog.dismiss();
                }
            });
            final ImageButton blackBtn = (ImageButton) brushDialog.findViewById(R.id.paint_black);
            blackBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imgView = blackBtn;
                    String color = blackBtn.getTag().toString();
                    drawView.setColor(color);
                    //      imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                    //    currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                    currPaint = blackBtn;
                    brushDialog.dismiss();
                }
            });

            drawView.setErase(false);
            drawView.setBrushSize(drawView.getLastBrushSize());
            //ImageButton paintBtn = (ImageButton)brushDialog.findViewById(R.id.paint_colors);
            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.eraser_chooser);
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();

                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();

                }
            });
            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        } else if (view.getId() == R.id.save_btn) {
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);

                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");

                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            saveDialog.show();
        } else if (view.getId() == R.id.btn_select_gallery) {
            // 카메라 호출
            galleryBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    // 이미지 잘라내기 위한 크기
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 150);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
                       // drawView.setErase(true);
                    } catch (ActivityNotFoundException e) {
                        // Do nothing for now
                    }

                }
            });

        } else if (view.getId() == R.id.cube_btn) {
            this.setContentView(glView);
            glView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    setContentView(R.layout.activity_main);

                }
            });
        } else if(view.getId() == R.id.highlighter_brush){
            drawView.highlighter();


        } else if(view.getId() == R.id.blur_brush){
            drawView.blur();

        }
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        if (requestCode == PICK_FROM_GALLERY) {
            Bundle extras2 = data.getExtras();
            if (extras2 != null) {
                Bitmap photo = extras2.getParcelable("data");
                imgview.setImageBitmap(photo);
            }
        } else{
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }}catch(Exception e) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                .show();
    }

    }

}