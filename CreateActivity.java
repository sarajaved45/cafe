package com.example.cafedesign;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private TextView sizeTextView;
    private TextView rotationTextView;
    private FirebaseFirestore mDatabase;
    private FirebaseStorage mStorage;
    private List<ImageView> draggedImages; // List to store dragged images
    private float currentRotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createactivity);

        // Initialize Firestore
        mDatabase = FirebaseFirestore.getInstance();
        // Initialize Firebase Storage
        mStorage = FirebaseStorage.getInstance();

        // Initialize draggedImages list
        draggedImages = new ArrayList<>();

        // Find views
        LinearLayout queueLayout = findViewById(R.id.queueLayout);
        TextView dropPlaceholder = findViewById(R.id.dropPlaceholder);
        LinearLayout dropZone = findViewById(R.id.dropZone);
        sizeTextView = findViewById(R.id.sizeTextView);
        rotationTextView = findViewById(R.id.rotationTextView);

        // Set up drag listeners for the queue items
        for (int i = 0; i < queueLayout.getChildCount(); i++) {
            View assetView = queueLayout.getChildAt(i);
            assetView.setOnTouchListener(new MyTouchListener());
        }

        // Set up drop listener for the drop zone
        dropZone.setOnDragListener(new MyDragListener(dropPlaceholder));

        // Set up save button listener
        Button saveButton = findViewById(R.id.buttonsave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDragCreations();
            }
        });
    }

    private final class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private class MyDragListener implements View.OnDragListener {

        private final TextView dropPlaceholder;

        MyDragListener(TextView dropPlaceholder) {
            this.dropPlaceholder = dropPlaceholder;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundResource(R.drawable.drop_zone_background);
                    dropPlaceholder.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.drop_zone_background_hover);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundResource(R.drawable.drop_zone_background);
                    break;
                case DragEvent.ACTION_DROP:
                    // Get the dragged view (ImageView)
                    ImageView draggedView = (ImageView) event.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    owner.removeView(draggedView);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(draggedView);
                    dropPlaceholder.setVisibility(View.INVISIBLE);
                    draggedView.setVisibility(View.VISIBLE);

                    // Update size and rotation based on user adjustments
                    updateSizeAndRotation(draggedView);

                    // Add the dragged image to the list
                    draggedImages.add(draggedView);

                    // Save image to Firebase Storage and metadata to Firestore
                    saveImageAndMetadata(draggedView);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(android.R.color.transparent);
                    break;
                default:
                    break;
            }
            return true;
        }

        private void saveImageAndMetadata(ImageView imageView) {
            Uri imageUri = (Uri) imageView.getTag();
            if (imageUri == null) {
                Log.e("CreateActivity", "Image URI is null for ImageView: " + imageView);
                return;
            }

            // Upload image to Firebase Storage and save metadata to Firestore
            uploadImageToFirebaseStorage(imageUri, new FirebaseStorageCallback() {
                @Override
                public void onSuccess(Uri imageUrl) {
                    // Image uploaded successfully, now save metadata to Firestore
                    Map<String, Object> creationMap = new HashMap<>();
                    creationMap.put("imageUrl", imageUrl.toString());
                    creationMap.put("rotation", imageView.getRotation());
                    creationMap.put("scaleX", imageView.getScaleX());
                    creationMap.put("scaleY", imageView.getScaleY());
                    // Add more data as needed

                    // Save creation to Firestore
                    saveCreationToFirestore(creationMap);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("CreateActivity", "Failed to upload image to Firebase Storage", e);
                    Toast.makeText(CreateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void updateSizeAndRotation(final View view) {
        sizeTextView.setText("Size: " + (int) (view.getScaleX() * 100) + "%");
        rotationTextView.setText("Rotation: " + (int) view.getRotation() + "°");

        SeekBar resizeSeekBar = findViewById(R.id.resizeSeekBar);
        resizeSeekBar.setMax(200);
        resizeSeekBar.setProgress((int) (view.getScaleX() * 100));

        resizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = progress / 100.0f;
                view.setScaleX(scale);
                view.setScaleY(scale);
                sizeTextView.setText("Size: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar rotateSeekBar = findViewById(R.id.rotateSeekBar);
        rotateSeekBar.setMax(360);
        rotateSeekBar.setProgress((int) view.getRotation());

        rotateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setRotation(progress);
                rotationTextView.setText("Rotation: " + progress + "°");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void saveDragCreations() {
        LinearLayout dropZone = findViewById(R.id.dropZone);
        int childCount = dropZone.getChildCount();
        List<Map<String, Object>> creations = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View child = dropZone.getChildAt(i);
            if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;

                // Check if tag is null
                if (imageView.getTag() == null) {
                    Log.e("CreateActivity", "Image tag is null for ImageView: " + imageView);
                    continue;
                }

                Uri imageUri = (Uri) imageView.getTag();
                if (imageUri == null) {
                    Log.e("CreateActivity", "Image URI is null for ImageView: " + imageView);
                    continue;
                }

                // Upload image to Firebase Storage and save metadata to Firestore
                uploadImageToFirebaseStorage(imageUri, new FirebaseStorageCallback() {
                    @Override
                    public void onSuccess(Uri imageUrl) {
                        // Image uploaded successfully, now save metadata to Firestore
                        Map<String, Object> creationMap = new HashMap<>();
                        creationMap.put("imageUrl", imageUrl.toString());
                        creationMap.put("rotation", imageView.getRotation());
                        creationMap.put("scaleX", imageView.getScaleX());
                        creationMap.put("scaleY", imageView.getScaleY());
                        // Add more data as needed

                        // Save creation to Firestore
                        saveCreationToFirestore(creationMap);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("CreateActivity", "Failed to upload image to Firebase Storage", e);
                        Toast.makeText(CreateActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // Start MyCreationActivity (corrected typo in class name)
        Intent intent = new Intent(CreateActivity.this, MyCreationActivit.class);
        startActivity(intent);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, final FirebaseStorageCallback callback) {
        // Create a storage reference
        StorageReference storageRef = mStorage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");

        // Upload file to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Get the download URL
                                callback.onSuccess(uri);
                            })
                            .addOnFailureListener(exception -> {
                                // Handle any errors getting the download URL
                                callback.onFailure(exception);
                            });
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    callback.onFailure(exception);
                });
    }

    private void saveCreationToFirestore(Map<String, Object> creation) {
        mDatabase.collection("creations")
                .add(creation)
                .addOnSuccessListener(documentReference -> {
                    Log.d("CreateActivity", "onSuccess: ");
                    Toast.makeText(CreateActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("CreateActivity", "Error saving image", e);
                    Toast.makeText(CreateActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                });
    }

    private class ManualDragTouchListener implements View.OnTouchListener {

        private float dX, dY;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    return true;
                default:
                    return false;
            }
        }
    }

    interface FirebaseStorageCallback {
        void onSuccess(Uri imageUrl);
        void onFailure(Exception e);
    }
}
