private void showSelectImageDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		
		builder.setTitle("Select Image");
		builder.setItems(getResources().getStringArray(R.array.select_image),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						int pos = (int) Math.round(item);

						// To open camera inten
						if (pos == 0) {
							Intent intent = new Intent(
									"android.media.action.IMAGE_CAPTURE");

							startActivityForResult(intent, 0);

						}
						// To open the gallery
						else if (pos == 1) {
							startActivityForResult(new Intent(
									Intent.ACTION_PICK).setType("image/*"), 1);
						}

						dialog.dismiss();
					}
				});

		AlertDialog alertBox = builder.create();
		alertBox.show();
	}


	//onActivityResult Associated with above code


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			if (data != null) {
				image = (Bitmap) data.getExtras().get("data");

				iv_image_place.setImageBitmap(image);
			}
		}

		else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			Cursor cursor_ob = getContentResolver().query(data.getData(),
					new String[] { MediaStore.Images.Media.DATA }, null, null,
					null);

			if (cursor_ob.moveToNext()) {
				String filepath = cursor_ob.getString(cursor_ob
						.getColumnIndex(MediaStore.Images.Media.DATA));

				image = BitmapFactory.decodeFile(filepath);

				/*
				 * imageNameOnly_array.add(filePath.substring(
				 * filePath.lastIndexOf("/") + 1, filePath.length()));
				 */

				/*
				 * imageNameOnly_array.add(filePath
				 * .substring(filePath.lastIndexOf("/") + 1,
				 * filePath.length()).replace('[', ' ') .replace(']',
				 * ' ').trim());
				 */

				// Log.d("31z", imageNameOnly_array + "");

				iv_image_place.setImageBitmap(image);
			}

		}
	}
  
  from:WhiteP
