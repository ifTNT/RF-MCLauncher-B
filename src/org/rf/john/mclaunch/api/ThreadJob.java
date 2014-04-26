package org.rf.john.mclaunch.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.tukaani.xz.XZInputStream;

public /**
 * 多執行緒工作分配器
 * @author john
 *
 */
class ThreadJob{
	private int JobCount=0;
	private int FinishedJobs=0;
	private int RunningJobs=0;
	private String Type;
	private String JobName;
	private ThreadJob ThisJob;
	private int o0o=0;
	private int o0oDelay=0;
	public static Logger logger;
	
	@SuppressWarnings("static-access")
	public void DownloadJob(Logger logger,String _jn,DownloadUtil Do){
		this.Type="Download";
		this.JobCount=1;
		this.FinishedJobs=0;
		logger.Info("Downloading");
		logger.progressBar.setIndeterminate(true);
		logger.progressBar.setString("Downloading");
		logger.progressBar.setMaximum(0);
		new DownloadThread(Do.DownloadPath,Do.FilePath,this,"Td1").start();
		while(this.FinishedJobs!=1){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				logger.Error("DownloadThreadJobError",e);
			}
		}
		return;
	}
	/**
	 * 開始多執行緒下載
	 * @param _JobName 工做名稱
	 * @param DownloadBaseURL 下載物件
	 */
	public void DownloadJob(Logger _logger,String _JobName,ArrayList<DownloadUtil> DownloadObjects){DownloadJob(_logger,_JobName,DownloadObjects.size(),DownloadObjects);}
	@SuppressWarnings("static-access")
	public void DownloadJob(final Logger _logger,String _JobName,int MaxThreads,ArrayList<DownloadUtil> DownloadObjects){
		this.logger=_logger;
		this.ThisJob=this;
		final ArrayList<DownloadUtil> NeedDownloads=new ArrayList<>();
		for(int i=0;i<=DownloadObjects.size()-1;i++/*int i=DownloadObjects.size()-1;i>=0;i--*/){
			DownloadUtil OneDownloadJob=DownloadObjects.get(i);
			if(!new File(OneDownloadJob.FilePath.replace("-universal","")).isFile()){NeedDownloads.add(OneDownloadJob);}
		}
		if(NeedDownloads.size()==1){
			DownloadJob(logger,_JobName,NeedDownloads.get(0));
			return;
		}
		this.Type="Download";
		this.JobCount=NeedDownloads.size();
		this.JobName=_JobName;
		if(MaxThreads>this.JobCount) MaxThreads=this.JobCount;
		logger.progressBar.setMaximum(this.JobCount);
		if(this.JobCount>1){
			logger.Info("\n---Starting job:["+this.JobName+"][Download]");
			logger.progressBar.setString("Downloading... ("+this.FinishedJobs+"/"+this.JobCount+")");
			//Logger.Info(" max thread:"+MaxThreads);
			
			final int _MaxThreads=MaxThreads/2;
		
			new Thread(){ //DownloadHub1 (0~[1/2-1])
				@Override
				public void run(){
					Thread.currentThread().setName("DownloadHub1");
					for(int i=0;i<=(JobCount/2)-1;i++){
						DownloadUtil OneDownloadJob=NeedDownloads.get(i);
						if(RunningJobs>=_MaxThreads){
							try {
								while(RunningJobs>_MaxThreads/2){
									Thread.sleep(1);
								}
								Thread.sleep(100);
							} catch (InterruptedException e) {
								logger.Error("Download thread job sleep error",e);
							}
						}
						new DownloadThread(OneDownloadJob.DownloadPath,OneDownloadJob.FilePath,ThisJob,"Td"+i).start();
						RegisterThread();						
					}
				}
			}.start();
			
			if(JobCount>=2)
			new Thread(){ //DownloadHub2 (1/2~1)
				@Override
				public void run(){
					Thread.currentThread().setName("DownloadHub2");
					for(int j=JobCount/2;j<=JobCount-1;j++){
						DownloadUtil OneDownloadJob=NeedDownloads.get(j);
						if(RunningJobs>=_MaxThreads){
							try {
								while(RunningJobs>_MaxThreads/2){
									Thread.sleep(1);
								}
								Thread.sleep(100);
							} catch (InterruptedException e) {
								logger.Error("Download thread job sleep error",e);
							}
						}
						new DownloadThread(OneDownloadJob.DownloadPath,OneDownloadJob.FilePath,ThisJob,"Td"+j).start();
						RegisterThread();
						
					}
				}
			}.start();
			while(this.FinishedJobs!=this.JobCount){
				if(o0oDelay==0){
					String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
					Logger.progressBar.setString("Downloading... ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
					o0o=(o0o+1)%3;
				}
				o0oDelay=(o0oDelay+1)%80;
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					logger.Error("Download thread job sleep error",e);
				}
			}
			logger.Info("---Finished job:["+this.JobName+"][Download]");
		}
		return;
	}
	
	/**
	 * 開始多執行緒解壓縮
	 * @param _JobName 工作名稱
	 * @param ExtractFile 欲解壓縮的檔案
	 * @param TargetDir 目標資料夾
	 * @param exclude 排除的檔案
	 */
	@SuppressWarnings("static-access")
	public void ExtractJob(Logger _logger,String _JobName,ArrayList<String> ExtractFile,ArrayList<String> TargetDir,ArrayList<ArrayList<String>> exclude){ //Extract
		this.logger=_logger;
		this.Type="Extract";
		this.JobCount=ExtractFile.size();
		this.JobName=_JobName;
		logger.progressBar.setMaximum(this.JobCount);
		logger.Info("\n---Starting job:[Extract]");
		logger.progressBar.setString("LoadingLib... ("+this.FinishedJobs+"/"+this.JobCount+")");
		logger.Info(" max thread:"+this.JobCount);
		for(int i=0;i<this.JobCount;i++){
			new ExtractThread(ExtractFile.get(i),TargetDir.get(i),exclude.get(i),this).start();
		}
		while(this.FinishedJobs!=this.JobCount){
			if(o0oDelay==0){
				String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
				logger.progressBar.setString("LoadingLib... ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
				o0o=(o0o+1)%3;
			}
			o0oDelay=(o0oDelay+1)%80;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				logger.Error("ExtractThreadJobError",e);
			}
		}
		logger.Info("---Finished job:["+this.JobName+"][Extract]");
		return;
	}
	private synchronized void RegisterThread(){
		this.RunningJobs++;
	}
	/**
	 * 單一執行緒結束工作時,所觸發的方法
	 */
	@SuppressWarnings("static-access")
	public synchronized void FinishJob(){
		this.FinishedJobs++;
		this.RunningJobs--;
		Logger.progressBar.setValue(FinishedJobs); //完成進度
		String Showo0o=(o0o==0?"0":"o")+(o0o==1?"0":"o")+(o0o==2?"0":"o");
		if(this.Type.equals("Download")){
			logger.progressBar.setString("Downloading... ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
		}else{
			logger.progressBar.setString("LoadingLib... ("+this.FinishedJobs+"/"+this.JobCount+")"+Showo0o);
		}
	}
}

class DownloadUtil{
	public String DownloadPath;
	public String FilePath;
	public DownloadUtil(){}
	public DownloadUtil(String _DownloadPath,String _FilePath){
		this.DownloadPath=_DownloadPath;
		this.FilePath=_FilePath;
	}
}
/**
 * 多執行緒下載實作類別
 * @author john
 *
 */
class DownloadThread extends Thread{
	private String FileURL="";
	private ThreadJob JobTable;
	private String DownloadURL="";
	private String ThreadName="";
	//private String RootDir;
	
	/**
	 * 單一執行緒工作設定
	 * @param DownloadHost 下載主機
	 * @param DownloadURL 下載路徑
	 * @param rootjob 工作分配器
	 */
	public DownloadThread(String _DownloadURL,String _FileURL,ThreadJob rootjob,String _ThreadName){
		this.FileURL=_FileURL;
		this.JobTable=rootjob;
		this.DownloadURL=_DownloadURL;
		this.ThreadName=_ThreadName;
		//this.RootDir=rootDir;
	}
	public static HttpURLConnection getConnectObj(String targetURL) throws IOException{
		URL targetUrlObj=new URL(targetURL);
		URLConnection urlc = targetUrlObj.openConnection();
		urlc.setConnectTimeout(5000);
		HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
		ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
		ConnectObj.connect();
		return ConnectObj;
	}
	@Override
	public void run(){
		Thread.currentThread().setName(this.ThreadName);
		
		if(this.FileURL.lastIndexOf(File.separatorChar)>0){
			String TargetDir=this.FileURL.substring(0,this.FileURL.lastIndexOf(File.separatorChar));
			
			new File(TargetDir).mkdirs();
		}
		JobTable.logger.Info("Starting download: "+this.FileURL.replace(Launcher.minecraftDir,"[MinecraftRoot]"+File.separatorChar));
		try{
			if(!new File(FileURL).isFile()){
				URL DownloadUrlObj = new URL(this.DownloadURL);
				URLConnection urlc = DownloadUrlObj.openConnection();
				urlc.setConnectTimeout(5000);
				HttpURLConnection ConnectObj = (HttpURLConnection) urlc;
				ConnectObj.addRequestProperty("User-Agent", "Mozilla/5.0");
				ConnectObj.connect();
				BufferedInputStream DownloadStream = new BufferedInputStream(ConnectObj.getInputStream());
				if(DownloadURL.matches(".*\\.pack\\.xz")){
					unpackLibrary(new File(FileURL),readFully(DownloadStream));
				}else{
					new File(FileURL).createNewFile();
					BufferedOutputStream WriteStream = new BufferedOutputStream(new FileOutputStream(FileURL));
					WriteStream.write(readFully(DownloadStream));
					WriteStream.flush();
					DownloadStream.close();
					WriteStream.close();
				}
			}
			JobTable.logger.Info("Finish download: "+this.FileURL.replace(Launcher.minecraftDir,"[MinecraftRoot]"+File.separatorChar));
		}catch(IOException e){
			JobTable.logger.Error("Download Error( "+FileURL+" )",e);
			
		}
		this.JobTable.FinishJob();
	}
	//------START 從ForgeInstaller複製過來的方法------
	public void unpackLibrary(File output, byte[] data) throws IOException {
		byte[] decompressed = readFully(new XZInputStream(new ByteArrayInputStream(data)));
		JobTable.logger.Info("Unpacking: "+output.getName()+".pack.xz");
		
		String end = new String(decompressed, decompressed.length - 4, 4);
		if (!end.equals("SIGN")){
			JobTable.logger.Error("Unpacking failed, signature missing " + end);
			return;
		}
	
		int x = decompressed.length;
		int len = decompressed[(x - 8)] & 0xFF | (decompressed[(x - 7)] & 0xFF) << 8 | (decompressed[(x - 6)] & 0xFF) << 16 | (decompressed[(x - 5)] & 0xFF) << 24;
	
		byte[] checksums = Arrays.copyOfRange(decompressed, decompressed.length - len - 8, decompressed.length - 8);
	
		FileOutputStream jarBytes;
			
		jarBytes = new FileOutputStream(output);
		JarOutputStream jos = new JarOutputStream(jarBytes);
	
		Pack200.newUnpacker().unpack(new ByteArrayInputStream(decompressed), jos);
	
		jos.putNextEntry(new JarEntry("checksums.sha1"));
		jos.write(checksums);
		jos.closeEntry();
	
		jos.close();
		jarBytes.close();
		JobTable.logger.Info("Finish unpack: "+output.getName()+".pack.xz => "+output.getName());
	}
	public static byte[] readFully(InputStream stream) throws IOException{
		byte[] data = new byte[4096];
		ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
		int len;
		do {
			len = stream.read(data);
			if (len > 0){
				entryBuffer.write(data, 0, len);
			}
		}while (len != -1);

		return entryBuffer.toByteArray();
	}
	public static byte[] readFully(ZipInputStream stream) throws IOException{
		byte[] data = new byte[4096];
		ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
		int len;
		do {
			len = stream.read(data);
			if (len > 0){
				entryBuffer.write(data, 0, len);
			}
		}while (len != -1);

		return entryBuffer.toByteArray();
	}
	//------END 從ForgeInstaller複製過來的方法------
}
/**
 * 多執行續解壓縮實作類別
 * @author john
 *
 */
class ExtractThread extends Thread{ 
    private String InputFile;
    private String TargetDir;
    //private Object[] Exclude;
    private HashSet<String> Exclude;
    private ThreadJob JobTable;
    /**
     * 單一執行續工作設定
     * @param inputfile 原始檔案
     * @param targetDir 目標資料夾
     * @param exclude 排除的檔案
     * @param rootjob 工作分配器
     */
	public ExtractThread(String inputfile,String targetDir,ArrayList<String> exclude,ThreadJob rootjob){
		this.InputFile=inputfile;
		this.TargetDir=targetDir;
		this.Exclude=new HashSet<String>(exclude);
		this.JobTable=rootjob;
	}
	@Override
	public void run(){
		if(!new File(this.TargetDir).isDirectory()){
			new File(this.TargetDir).mkdirs();
		}/*
		FileOutputStream fileOut; 
        File file; 
        InputStream inputStream; 
		Arrays.sort(this.Exclude);*/
        try{
        	ZipInputStream ZipInput = new ZipInputStream(new FileInputStream(new File(this.InputFile)));
        	ZipEntry OneEntry = ZipInput.getNextEntry();
        	
        	while(OneEntry!=null){
        		String TargetFileName=OneEntry.getName();
        		if(this.Exclude.contains(TargetFileName)||TargetFileName.matches(".*/.*")){
        			JobTable.logger.Info("Skip: "+TargetFileName);
        			OneEntry=ZipInput.getNextEntry();
        			continue;
        		}
        		JobTable.logger.Info("Extracting: "+TargetFileName);
        		if(!new File(this.TargetDir+File.separatorChar+TargetFileName).isFile())
        			new File(this.TargetDir+File.separatorChar+TargetFileName).createNewFile();
        		FileOutputStream TargetFileStream=new FileOutputStream(new File(this.TargetDir+File.separatorChar+TargetFileName));
        		TargetFileStream.write(DownloadThread.readFully(ZipInput));
        		TargetFileStream.close();
        		OneEntry=ZipInput.getNextEntry();
        	}
        	ZipInput.closeEntry();
        	ZipInput.close();
        	
        }catch(IOException e){
        	JobTable.logger.Error("Extract Error",e);
        }
        this.JobTable.FinishJob();
	}
}