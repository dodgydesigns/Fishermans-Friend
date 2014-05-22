package com.dodgydesigns.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import com.dodgydesigns.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{

	int mGalleryItemBackground;
	private Context mContext;
	private Uri[] mUrls;
	String[] mFiles = null;
	private File[] imagelist;

	public ImageAdapter( Context c, File imageRootDir )
	{
		mContext = c;

		getImageList( imageRootDir );

		TypedArray a = c.obtainStyledAttributes( R.styleable.gallery );
		mGalleryItemBackground =
		    a.getResourceId( R.styleable.gallery_android_galleryItemBackground, 0 );
		a.recycle();
	}

	private void getImageList( File imageRootDir )
	{
		FilenameFilter filenameFilter = new FilenameFilter()
		{
			@Override
			public boolean accept( File dir, String name )
			{
				return ((name.endsWith( ".jpg" )) || (name.endsWith( ".png" )));
			}
		};

		imagelist = imageRootDir.listFiles( filenameFilter );

		mFiles = new String[imagelist.length];

		for( int i = 0; i < imagelist.length; i++ )
		{
			mFiles[i] = imagelist[i].getAbsolutePath();			
		}

		mUrls = new Uri[mFiles.length];

		for( int i = 0; i < mFiles.length; i++ )
		{
			mUrls[i] = Uri.parse( mFiles[i] );
		}
	}

	public String getItemAbsPath(int position)
	{
		return mFiles[position];
	}
	
	public String getImageName(int position)
	{
		return imagelist[position].getName();
	}
	
	@Override
	public Object getItem( int position )
	{
		return position;
	}

	@Override
	public long getItemId( int position )
	{
		return position;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		ImageView i = new ImageView( mContext );
		
		Bitmap scaledImage = decodeFile( new File( mFiles[position]  ) );

		i.setImageBitmap( scaledImage );
		i.setScaleType( ImageView.ScaleType.FIT_XY );
		i.setLayoutParams( new Gallery.LayoutParams( scaledImage.getWidth(), scaledImage.getHeight() ) );

		return i;
	}

	@Override
	public int getCount()
	{
		return mFiles.length;
	}

	private Bitmap decodeFile( File f )
	{
		try
		{
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream( new FileInputStream( f ), null, o );

			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while( true )
			{
				if( width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE )
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			
			return BitmapFactory.decodeStream( new FileInputStream( f ), null, o2 );
		}
		catch( FileNotFoundException e )
		{
		}
		return null;
	}
}
