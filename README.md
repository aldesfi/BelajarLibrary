Belajar Membuat Library Upload File 



Dokumentasi Library Upload Document


objectHasil = aldes.doUpload(
                        aldes.fileDeclaration(imagePath1),
                        imagePath1,
                        objectUrl,
                        fileName1 
            		);

aldes.printHasil(
        objectHasil,
        textView1);


1.	Operation Upload

aldes.doUpload

      -	Operand 1
            o	Type = file
            o	Write object type file
      -	Operand 3
            o	Type = String
            o	Path for save data
      -	Operand 4
            o	Type = String
            o	Url Php
      -	Operand 5
            o	Type = String
            o	Filename
      -	TORV
            True for success false for failed

2.	Operation SetText For Result

      aldes.printHasil

        -	Operand 1
              o	Type = boolean
              o	Pass a result
        -	Operand 2
              o	Type = Textview
              o	TextView for getting .setText
        -	TORV
              Void





PHP CODE


    $file_path = "uploads/";
    $file_path = $file_path . basename($_FILES['uploaded_file']['name']);
    $data = $_FILES['uploaded_file']['name'];
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }
      //GETLOG
      $fp = fopen('data.txt', 'w');
      fwrite($fp, "Data : $data");
      fclose($fp);

