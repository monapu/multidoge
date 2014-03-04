package org.multibit.utils;

import org.apache.commons.io.FileUtils;
// import org.apache.commons.io.filefilter.FileFilterUtils
import java.io.File;

import org.multibit.platform.builder.OSUtils;
import java.util.Collection;

import org.multibit.file.BackupManager;
import org.multibit.model.bitcoin.BitcoinModel;

public class FilePermissionUtils {
    public static void setReadableOwnerOnly( File f ){
        if( !OSUtils.isWindows() ){
            try {
                if( !f.exists() )
                    f.createNewFile();
                f.setReadable( false , false );
                f.setReadable( true  , true  );
            } catch(SecurityException se){

            } catch(java.io.IOException e){

            }
        }
    }

    public static void setWritableOwnerOnly( File f ){
        if( !OSUtils.isWindows() ){
            try {
                if( !f.exists() )
                    f.createNewFile();
                f.setWritable( false , false );
                f.setWritable( true  , true  );
            } catch(SecurityException se){

            } catch(java.io.IOException e){

            }
        }
    }

    public static void setWalletPermission( File f ){
        setReadableOwnerOnly(f);
        setWritableOwnerOnly(f);
    }

    private static final String[] WALLET_EXTENSIONS = 
    {BitcoinModel.WALLET_FILE_EXTENSION,
     BitcoinModel.WALLET_FILE_EXTENSION + "." + BackupManager.FILE_ENCRYPTED_WALLET_SUFFIX,
     BackupManager.INFO_FILE_SUFFIX_STRING,
     BitcoinModel.PRIVATE_KEY_FILE_EXTENSION,
    };
    
    public static Collection<File> findAllWalletFiles(File dir){
        return FileUtils.listFiles( dir , WALLET_EXTENSIONS , true );
    }

    public static void setWalletPermissionAll( File dir ){
        if(!OSUtils.isWindows()){
            Collection<File> files = findAllWalletFiles(dir);
            for(File f : files){
                setWalletPermission( f );
            }
        }
    }

}