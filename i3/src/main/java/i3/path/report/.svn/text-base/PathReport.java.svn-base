package i3.path.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.phprpc.util.PHPSerializer;

import ControlPanelDataOp.CommInfo;
import ControlPanelDataOp.FromView;
import ControlPanelDataOp.Source;
import ControlPanelDataOp.Top;

public class PathReport {

	private static final Log LOG = LogFactory.getLog(PathReport.class);

	private String src = "/tmp/path/";

	private String dest = "/tmp/path/track/";

	private String date = "20101213";

	public PathReport() {
		new File(dest).mkdirs();
	}

	public void from() throws IllegalArgumentException, IOException,
			IllegalAccessException, InvocationTargetException {
		File[] files = listFiles(src + date + "_path_from_top/");

		for (File file : files) {
			String fileName = file.getName();
			LOG.info(fileName);

			FromView fromView = null;
			String nick = null;
			int i = 0;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				LOG.info(line);

				String[] tmp = line.split("\\s");
				if (nick == null) {
					nick = tmp[0];
					i++;

					fromView = buildFromView(tmp);
				} else if (i < 5 && nick.equals(tmp[0])) {
					Source source = new Source();
					source.setSourceID(tmp[1]);
					source.setPv(tmp[2]);
					source.setUv(tmp[3]);
					source.setTopList(new ArrayList<Top>());

					List<Source> sourceList = fromView.getSourceList();
					sourceList.add(source);
				} else if (!nick.equals(tmp[0])) {
					byte[] php = new PHPSerializer().serialize(fromView);
					LOG.debug(new String(php));

					FileOutputStream out = new FileOutputStream(dest + nick);
					out.write(php);
					out.close();

					nick = tmp[0];
					i = 0;

					fromView = buildFromView(tmp);
				} else {
					nick = tmp[0];
				}
			}

			reader.close();
		}
	}

	public void path1() throws IllegalArgumentException, IOException,
			IllegalAccessException, InvocationTargetException {
		File[] files = listFiles(src + date + "_path_1_top/");

		for (File file : files) {
			String fileName = file.getName();
			LOG.info(fileName);

			FromView fromView = null;
			String nick = null;
			int i = 0;
			Map<String, Source> sources = new HashMap<String, Source>();

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				LOG.info(line);

				String[] tmp = line.split("\\s");
				if (nick == null) {
					nick = tmp[0];

					fromView = buildFromView(nick);
					if (fromView == null) {
						nick = null;
						continue;
					}
					List<Source> sourceList = fromView.getSourceList();
					for (Source source : sourceList) {
						sources.put(source.getSourceID(), source);
					}

					CommInfo commInfo = new CommInfo();
					commInfo.setFromType(tmp[2]);
					commInfo.setUrl(tmp[4]);
					commInfo.setTitle(tmp[5]);
					commInfo.setPv(tmp[6]);
					commInfo.setUv(tmp[7]);

					Top top = new Top();
					top.setCommInfo(commInfo);

					Source source = sources.get(tmp[1]);
					if (source != null) {
						source.getTopList().add(top);
						i++;
					}
				} else if (!nick.equals(tmp[0])) {
					byte[] php = new PHPSerializer().serialize(fromView);
					LOG.debug(new String(php));

					FileOutputStream out = new FileOutputStream(dest + nick);
					out.write(php);
					out.close();

					nick = tmp[0];
					i = 0;

					fromView = buildFromView(nick);
					if (fromView == null) {
						nick = null;
						continue;
					}

					List<Source> sourceList = fromView.getSourceList();
					for (Source source : sourceList) {
						sources.put(source.getSourceID(), source);
					}

					CommInfo commInfo = new CommInfo();
					commInfo.setFromType(tmp[2]);
					commInfo.setUrl(tmp[4]);
					commInfo.setTitle(tmp[5]);
					commInfo.setPv(tmp[6]);
					commInfo.setUv(tmp[7]);

					Top top = new Top();
					top.setCommInfo(commInfo);

					Source source = sources.get(tmp[1]);
					if (source != null) {
						source.getTopList().add(top);
						i++;
					}
				} else if (i < 5) {
					Source source = new Source();
					source.setSourceID(tmp[1]);
					source.setPv(tmp[2]);
					source.setUv(tmp[3]);

					List<Source> sourceList = fromView.getSourceList();
					sourceList.add(source);
				} else {
					continue;
				}
			}

			reader.close();
		}
	}

	public void path2() throws IllegalArgumentException, IOException,
			IllegalAccessException, InvocationTargetException {
		File[] files = listFiles(src + date + "_path_2_top/");

		for (File file : files) {
			String fileName = file.getName();
			LOG.info(fileName);

			FromView fromView = null;
			String nick = null;
			int i = 0;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				LOG.info(line);

				String[] tmp = line.split("\\s");
				if (nick == null) {
					nick = tmp[0];
					i++;

					fromView = buildFromView(nick);
					if (fromView == null) {
						nick = null;
						continue;
					}
					List<Source> sourceList = fromView.getSourceList();
					for (Source source : sourceList) {

					}

				} else if (!nick.equals(tmp[0])) {
					byte[] php = new PHPSerializer().serialize(fromView);
					LOG.debug(new String(php));

					FileOutputStream out = new FileOutputStream(dest + nick);
					out.write(php);
					out.close();

					nick = tmp[0];
					i = 0;

					fromView = buildFromView(tmp);
				} else if (i < 5) {
					Source source = new Source();
					source.setSourceID(tmp[1]);
					source.setPv(tmp[2]);
					source.setUv(tmp[3]);
					source.setTopList(new ArrayList<Top>());

					List<Source> sourceList = fromView.getSourceList();
					sourceList.add(source);
				} else {
					continue;
				}
			}

			reader.close();
		}
	}

	private File[] listFiles(String f) {
		File path = new File(f);
		if (!path.exists()) {
			LOG.info("file: [" + f + "] not ready");
			return null;
		}

		File[] files = path.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isFile() && file.getName().startsWith("part-r-");
			}

		});
		return files;
	}

	private FromView buildFromView(String[] tmp) {
		Source source = new Source();
		source.setSourceID(tmp[1]);
		source.setPv(tmp[2]);
		source.setUv(tmp[3]);
		source.setTopList(new ArrayList<Top>());

		List<Source> sourceList = new ArrayList<Source>();
		sourceList.add(source);

		FromView fromView = new FromView();
		fromView.setSourceList(sourceList);

		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		try {
			fromView.setFromViewDate(formater.parse(date));
		} catch (ParseException e) {
			LOG.error("", e);
		}

		return fromView;
	}

	private FromView buildFromView(String nick) throws IllegalAccessException,
			InvocationTargetException {
		byte[] php = new byte[0];

		byte[] buffer = new byte[4096];
		FileInputStream in = null;
		try {
			File f = new File(dest + nick);
			if (!f.exists()) {
				return null;
			}

			in = new FileInputStream(f);
			int len = 0;
			while ((len = in.read(buffer)) > -1) {
				php = ArrayUtils.addAll(php, Arrays.copyOf(buffer, len));
			}
		} catch (FileNotFoundException e) {
			LOG.error("", e);
			return null;
		} catch (IOException e) {
			LOG.error("", e);
			return null;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				LOG.error("", e);
				return null;
			}
		}

		return (FromView) new PHPSerializer().unserialize(php, FromView.class);
	}

	public static void main(String[] args) throws IOException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		PathReport report = new PathReport();
		report.from();
		// report.path1();
		// report.path2();
	}

}
