package com.karoliinaoksanen.qrreader;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

/**
 * GUI for the QR code reader.
 * 
 * Copyright 2016 Bianca Chan
 * 
 * @author Bianca Chan bianca.chan.cc@gmail.com
 *
 */

public class GUI extends JFrame {

	// auto-generated serial version UID
	private static final long serialVersionUID = -3419095517174994786L;

	private JPanel panel;
	private JLabel statusLabel;
	private JButton pdfFileButton;
	private JButton saveResultsButton;
	private JButton decodeButton;
	private JButton addDataButton;
	private JLabel pdfFileField;
	// private JTextField resultsFileField;
	private JTextArea resultsArea;
	private JTextArea unreadablePagesArea;
	private ProgressMonitor progressMonitor;
	private Task task;

	private QRDecoder decoder;
    private QRReader qrReader;

	class Task extends SwingWorker<Void, Void> {
		private int numberOfPages = decoder.getNumberOfPages();

		@Override
		protected Void doInBackground() throws Exception {
			int progress = 0;
			setProgress(0);
			try {
				statusLabel.setText("Decoding, please wait...");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				class DecodeThread extends Thread {
					public void run() {
						decoder.processFile();
					}
				}

				DecodeThread dt = new DecodeThread();
				dt.start();

				Thread.sleep(2000);
				while (progress < 100 && !isCancelled()) {
					// get progress from decoder
					progress = (int) (((double) decoder.getCurrentPage() / numberOfPages) * 100);
					setProgress(Math.min(progress, 100));
				}

				dt.join();
			} catch (InterruptedException ignore) {
			}

			return null;
		}

		@Override
		public void done() {
			statusLabel.setText("Ready");
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			addDataButton.setEnabled(true);

			// print decoded data and unreadable pages in text areas
			resultsArea.setText("Results:\n\n");
			unreadablePagesArea.setText("Unreadable pages: ");
			String newline = System.getProperty("line.separator");
			for (String str : decoder.getDecodedData())
				resultsArea.append(str + newline);
			ArrayList<Integer> badPages = decoder.getBadPages();
			for (int i = 0; i < badPages.size() - 1; i++)
				unreadablePagesArea.append(badPages.get(i) + ", ");
			unreadablePagesArea.append(badPages.get(badPages.size() - 1)
					.toString());
			resultsArea.setCaretPosition(0);
			unreadablePagesArea.setCaretPosition(0);
			
		}

	}

	public static void main(String[] args) {
		GUI window = new GUI();
		window.setVisible(true);
	}

	public GUI() {
		createComponents();
	}

	/**
	 * Creates the components of the GUI.
	 */
	private void createComponents() {
		this.setTitle("云天图文自动重命名工具");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// create buttons
		pdfFileButton = new JButton("选择文件的根目录...");
		saveResultsButton = new JButton("Save results");
		decodeButton = new JButton("开始重命名");
		addDataButton = new JButton("Manually insert data");

		class DirectoryChoserListener implements ActionListener {
			JFileChooser chooser = new JFileChooser("/Users/surecc/Documents/Program/ytty/建2所");
			// set only can chose the folders

			public void actionPerformed(ActionEvent event){
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				String selectPath;

				int returnVal = chooser.showOpenDialog(panel);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					selectPath = chooser.getSelectedFile().getPath();
					pdfFileField.setText(selectPath);
                    Vector<String> vecFile = traverseFolder(selectPath);
					String pdfFiles = "";
					for (int i = 0; i < vecFile.size(); i++) {
						pdfFiles = pdfFiles + vecFile.get(i) + "\n";
					}
					resultsArea.setText(vecFile.size() + "个文件待重命名\n" + pdfFiles);
					// enable the execute button
					decodeButton.setEnabled(true);
				}
			}

            public Vector<String> traverseFolder(String path) {
                Vector<String> vecFile = new Vector<String>();
                int fileNum = 0, folderNum = 0;
                File file = new File(path);
                if (file.exists()) {
                    LinkedList<File> list = new LinkedList<File>();
                    File[] files = file.listFiles();
                    for (File file2 : files) {
                        if (file2.isDirectory()) {
                            System.out.println("文件夹:" + file2.getAbsolutePath());
                            list.add(file2);
                            fileNum++;
                        } else {
                            System.out.println("文件:" + file2.getAbsolutePath());
                            if(file2.getAbsolutePath().trim().toLowerCase().endsWith(".pdf")) {
                                vecFile.add(file2.getAbsolutePath());
                            }
                            folderNum++;
                        }
                    }
                    File temp_file;
                    while (!list.isEmpty()) {
                        temp_file = list.removeFirst();
                        files = temp_file.listFiles();
                        for (File file2 : files) {
                            if (file2.isDirectory()) {
                                System.out.println("文件夹:" + file2.getAbsolutePath());
                                list.add(file2);
                                fileNum++;
                            } else {
                                System.out.println("文件:" + file2.getAbsolutePath());
                                if(file2.getAbsolutePath().trim().toLowerCase().endsWith(".pdf")) {
                                    vecFile.add(file2.getAbsolutePath());
                                }
                                folderNum++;
                            }
                        }
                    }
                } else {
                    System.out.println("文件不存在!");
                }
                return vecFile;
            }

			public Vector<String> GetPdfFileName(String fileAbsolutePath) {
				Vector<String> vecFile = new Vector<String>();
				File file = new File(fileAbsolutePath);
				File[] subFile = file.listFiles();

				for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                    String filePath = subFile[iFileLength].getAbsolutePath();
					// 判断是否为文件夹
					if (!subFile[iFileLength].isDirectory()) {
						// 判断是否为.pdf结尾
                        String fileName = subFile[iFileLength].getName();
						if (fileName.trim().toLowerCase().endsWith(".pdf")) {
							vecFile.add(filePath);
						}
//                        vecFile.add(filePath);
					}
				}
				return vecFile;
			}
		}

		class SaveResultsListener implements ActionListener {
			final JFileChooser fc = new JFileChooser();
			boolean init = true;

			public void actionPerformed(ActionEvent event) {
				if (decoder.getDecodedData().size() == 0) {
					JOptionPane.showMessageDialog(panel, "No results to save.");
				} else {
					if(init) { // only set filters once per program run
					FileNameExtensionFilter txtFilter = new FileNameExtensionFilter(
							"Text files", "txt");
					FileNameExtensionFilter csvFilter = new FileNameExtensionFilter(
							"CSV files", "csv");
					fc.setFileFilter(csvFilter);
					fc.addChoosableFileFilter(txtFilter);
					init = false;
					}
					int returnVal = fc.showSaveDialog(panel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File resultsFile = fc.getSelectedFile();
						decoder.setResultsFile(resultsFile);
						String resultsFileName = resultsFile.getName();
						int len = resultsFileName.length();
						String format = resultsFileName.substring(len - 3, len);
						if(!(format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("txt"))) {
							JOptionPane.showMessageDialog(panel, "Supported file formats: .csv and .txt");
							statusLabel.setText("Failed to save results");
						} else {
							decoder.saveResults();
							statusLabel.setText("Saved " + resultsFile.getName());
						}
					}
				}
			}

		}

		class AddDataListener implements ActionListener {

			public void actionPerformed(ActionEvent event) {
				// show dialog in which user can input data
				String newData = JOptionPane.showInputDialog("Enter data:");
				decoder.addResult(newData);
				// update results pane to include new data
				resultsArea.append(newData + "\n");
				resultsArea.setCaretPosition(resultsArea.getText().length());
			}

		}

		ActionListener folderListener = new DirectoryChoserListener();
		pdfFileButton.addActionListener(folderListener);

//		ActionListener pdfListener = new FileChooserListener();
//		pdfFileButton.addActionListener(pdfListener);

		ActionListener resultsListener = new SaveResultsListener();
		saveResultsButton.addActionListener(resultsListener);

		ActionListener dataListener = new AddDataListener();

		addDataButton.addActionListener(dataListener);
		addDataButton.setEnabled(false); // enable after a file has been decoded

		// get the qrdecoder and renames
		class ReadListener implements ActionListener, PropertyChangeListener {
			public void actionPerformed(ActionEvent event) {
				// 解码
			}

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
//				if ("progress" == evt.getPropertyName()) {
//					int progress = (Integer) evt.getNewValue();
//					progressMonitor.setProgress(progress);
//					String message = String.format("Completed %d%%.\n",
//							progress);
//					progressMonitor.setNote(message);
//					if (progressMonitor.isCanceled() || task.isDone()) {
//						if (progressMonitor.isCanceled()) {
//							task.cancel(true);
//							statusLabel.setText("Decoding canceled.");
//						} else {
//							statusLabel.setText("Decoding completed.");
//						}
//					}
//				}

			}
		}

        // change pdf into jpg and get the qrinfo and rename
        class ReaderListener implements ActionListener, PropertyChangeListener {
            public void actionPerformed(ActionEvent event) {
                String textArea = resultsArea.getText();
                if (!textArea.isEmpty()) {
                    // 1.Convert the Pdf files into JPG files
                    String[] list_pdf = textArea.split("\\n");
                    String[] qrList;
                    String oldPdfPath;
                    String newPdfAbsoluteName;
                    String oldName;
                    String imgName;
                    String resultQR;
                    QRReader qrReader = new QRReader();
                    Pdf2IMG pdf2img = new Pdf2IMG();

                    System.out.print("list_pdf.length() = " + list_pdf.length + "\n");
                    for (int i = 1; i < list_pdf.length; i++) {
                        if (!list_pdf[i].contains("pdf"))
                            continue;
                        oldName = list_pdf[i];
                        System.out.println(oldName);
                        imgName = pdf2img.pdf2img2(oldName);

                        // 2.Read
                        try{
                            resultQR = qrReader.readQRCodePub(imgName);
                        }
                        catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            File fileImg = new File(imgName);
                            fileImg.delete();
                            continue;
                        }
                        if (!resultQR.isEmpty()) {
                            System.out.println("[QR Code Info]" + resultQR);
                            qrList = resultQR.split("\\|");
                            resultQR = qrList[qrList.length - 3];
                            System.out.println("[QR Code Name]" + resultQR);
                            // 3.Rename
                            File fileOldPdf = new File(oldName);
                            oldPdfPath = fileOldPdf.getParent();
                            newPdfAbsoluteName = oldPdfPath + File.separator + resultQR + ".pdf";
                            File fileNewPdf = new File(newPdfAbsoluteName);
                            fileOldPdf.renameTo(fileNewPdf);
                            // 4.Remove the img file
                            File fileImg = new File(imgName);
                            fileImg.delete();
                        }
                    }
                }
            }

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        }




		// listener for both the action on the decode button and the property
		// changes of the progress monitor
		class DecodeListener implements ActionListener, PropertyChangeListener {
			public void actionPerformed(ActionEvent event) {
				if (decoder.hasOpenPdf()) {
					// decode pdf
					try {
						// create progress monitor
						progressMonitor = new ProgressMonitor(decodeButton,
								"Decoding file...", "", 0, 100);
						progressMonitor.setProgress(0);
						progressMonitor.setMillisToDecideToPopup(0);
						progressMonitor.setMillisToPopup(0);
						// create task, which calls decoder.processFile()
						task = new Task();
						task.addPropertyChangeListener(this);
						task.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					int progress = (Integer) evt.getNewValue();
					progressMonitor.setProgress(progress);
					String message = String.format("Completed %d%%.\n",
							progress);
					progressMonitor.setNote(message);
					if (progressMonitor.isCanceled() || task.isDone()) {
						if (progressMonitor.isCanceled()) {
							task.cancel(true);
							statusLabel.setText("Decoding canceled.");
						} else {
							statusLabel.setText("Decoding completed.");
						}
					}
				}

			}
		}



//		ActionListener dlistener = new DecodeListener();
        ReaderListener dlistener = new ReaderListener();
		decodeButton.addActionListener(dlistener);
		decodeButton.setEnabled(false); // disabled until PDF file has been
										// opened

		// create text fields for filenames
		pdfFileField = new JLabel();
		pdfFileField.setText(" ");

		JPanel pdfFilePanel = new JPanel();
		pdfFilePanel.setLayout(new GridLayout(1, 2, 20, 20));
		pdfFilePanel.add(pdfFileButton);
		pdfFilePanel.add(pdfFileField);

		JPanel decodePanel = new JPanel();
		decodePanel.setLayout(new GridLayout(1, 2, 20, 20));
		decodePanel.add(decodeButton);
		decodePanel.add(saveResultsButton);
		decodePanel.add(addDataButton);

		// arrange buttons and text fields in panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 1, 20, 20));
		topPanel.add(pdfFilePanel);
		topPanel.add(decodePanel);
		topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

		// create text areas for results and put them inside scroll panes
		resultsArea = new JTextArea("结果:\n\n", 20, 30);
		unreadablePagesArea = new JTextArea("Unreadable pages: ", 3, 30);
		resultsArea.setEditable(false);
		unreadablePagesArea.setEditable(false);
		unreadablePagesArea.setLineWrap(true);
		unreadablePagesArea.setWrapStyleWord(true);
		JScrollPane resultsScrollPane = new JScrollPane(resultsArea);
		JScrollPane unreadablePagesScrollPane = new JScrollPane(
				unreadablePagesArea);

		// arrange text areas in a panel
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BorderLayout());
		resultsPanel.setBorder(new EmptyBorder(5, 20, 5, 20));
		resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);
		resultsPanel.add(unreadablePagesScrollPane, BorderLayout.SOUTH);

		// create status bar
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(panel.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("Ready");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		// add components to main panel
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(resultsPanel, BorderLayout.CENTER);
		panel.add(statusPanel, BorderLayout.SOUTH);

		this.add(panel);
		this.pack();
	}

}
