package com.dodgydesigns.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.dodgydesigns.R;

public class FileBrowserDialog extends Dialog
{
	private enum DISPLAYMODE
	{
		ABSOLUTE, RELATIVE;
	}

	private final DISPLAYMODE displayMode = DISPLAYMODE.ABSOLUTE;
	private List<String> directoryEntries = new ArrayList<String>();
	private File currentDirectory = new File( "/" );

	private Context parent;
	
	private String chosenDirectory;

	public FileBrowserDialog( Context context )
	{
		super( context );

		parent = context;

		browseToRoot();
	}


	/**
	 * This function browses to the root-directory of the file-system.
	 */
	private void browseToRoot()
	{
		browseTo( new File( "/" ) );
	}


	private void browseTo( final File aDirectory )
	{
		if( aDirectory.isDirectory() )
		{
			this.currentDirectory = aDirectory;
			fill( aDirectory.listFiles() );
		}
		else
		{
			OnClickListener okButtonListener = new OnClickListener()
			{
				// @Override
				public void onClick( DialogInterface arg0, int arg1 )
				{
					chosenDirectory = aDirectory.getAbsolutePath();
				}
			};
			OnClickListener cancelButtonListener = new OnClickListener()
			{
				// @Override
				public void onClick( DialogInterface arg0, int arg1 )
				{
					// Do nothing
				}
			};
		}
	}

	private void fill( File[] files )
	{
		this.directoryEntries.clear();

		// Add the "." == "current directory"
		// and the ".." == 'Up one level'
		this.directoryEntries.add( parent.getString( R.string.current_dir ) );
		if( this.currentDirectory.getParent() != null )
			this.directoryEntries.add( parent.getString( R.string.up_one_level ) );

		switch( this.displayMode )
		{
			case ABSOLUTE:
				for( File file : files )
				{
					this.directoryEntries.add( file.getPath() );
				}
				break;
			case RELATIVE: // On relative Mode, we have to add the current-path to the beginning
				int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
				for( File file : files )
				{
					this.directoryEntries.add( file.getAbsolutePath().substring(
					                                                             currentPathStringLenght ) );
				}
				break;
		}
	}
}
