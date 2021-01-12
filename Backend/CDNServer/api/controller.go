package api

import (
	"CDNServer/ImgHandle"
	"CDNServer/StorageHandle"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"path/filepath"
	"strconv"

	"github.com/gin-gonic/gin"
)

const BaseURLAvatar = "https://avatars.dicebear.com/4.5/api/avataaars/"
const ROBOT_Avatar = "https://avatars.dicebear.com/4.5/api/bottts/"

type KeyPairValue struct {
	key   string
	value string
}

var ConfigAvatarRandom = []KeyPairValue{
	KeyPairValue{
		key:   "eyes[]",
		value: "happy",
	},
	KeyPairValue{
		key:   "mouth[]",
		value: "smile",
	},
	KeyPairValue{
		key:   "width",
		value: "100",
	},
	KeyPairValue{
		key:   "height",
		value: "100",
	},
}

func UploadFiles(ctx *gin.Context) {
	form, err := ctx.MultipartForm()
	if err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"msg": err})
		return
	}
	files := form.File["files"]
	fileNameReponse := make(map[string]string)
	for _, file := range files {
		filename := filepath.Base(file.Filename)
		if path, err := StorageHandle.CreateFileAutoFolder(filename, file); err != nil {
			ctx.JSON(http.StatusBadRequest, gin.H{"msg": err})
			return
		} else {
			fileNameReponse[filename] = path
		}
	}

	ctx.JSON(http.StatusOK, fileNameReponse)
}

func UploadFile(ctx *gin.Context) {
	file, err := ctx.FormFile("file")
	if err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"msg": err})
		return
	}

	filename := filepath.Base(file.Filename)
	if path, err := StorageHandle.CreateFileAutoFolder(filename, file); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"msg": err})
		return
	} else {
		ctx.JSON(http.StatusOK, gin.H{filename: path})
	}

}

func DownloadFile(ctx *gin.Context) {
	filename := ctx.Param("filename")

	//re := regexp.MustCompile("(.+?)(\\.[^.]*$|$)")
	//match := re.FindStringSubmatch(filename)
	//ext := match[2]

	path := StorageHandle.FindFileinStorage(filename)
	if path == "" {
		ctx.JSON(404, gin.H{"msg": "file non exist"})

		return
	}

	w, _ := strconv.ParseUint(ctx.Query("width"), 10, 64)
	h, _ := strconv.ParseUint(ctx.Query("height"), 10, 64)

	width := uint(w)
	height := uint(h)

	if width == 0 && height == 0 {
		// if !IsAttachment(ext) {
		// 	//fmt.Println("test")
		// 	ctx.File(path)
		// } else {
		ctx.File(path)
		//}
		return
	} else {
		patht, err := ImgHandle.Resize(path, width, height)
		if err != nil {
			ctx.JSON(http.StatusBadRequest, gin.H{"msg": err})
		} else {
			ctx.File(patht)
		}
		return
	}
}

func IsAttachment(ext string) bool {
	switch ext {
	case ".png":
	case ".jpg":
	case ".jpeg":
		return false
	}
	return true

}

func GetAvatarRandom(ctx *gin.Context) {

	// ctx.JSON(http.StatusOK, gin.H{"msg": "urlQuery"})
	// fmt.Println("urlQuery")
	// return

	GUID := StorageHandle.GenerationGUID()
	sex := ctx.Param("sex")
	urlQuery, err := url.Parse(BaseURLAvatar + GUID + ".svg")
	if err != nil {
		fmt.Println(err)
		return
	}
	var q url.Values = make(url.Values)

	//Set value default avatar
	for i := 0; i < len(ConfigAvatarRandom); i++ {
		q.Set(ConfigAvatarRandom[i].key, ConfigAvatarRandom[i].value)
	}

	//Set sex for avatar
	if sex == "woman" {
		q.Set("top[]", "longHair")
	} else if sex == "men" {
		q.Set("top[]", "shortHair")
	} else {
		q.Set("top[]", "shortHair")
		// urlQuery, err = url.Parse(ROBOT_Avatar + GUID + ".svg")
		// if err != nil {
		// 	fmt.Println(err)
		// 	return
		// }
	}

	urlQuery.RawQuery = q.Encode()
	fmt.Println(urlQuery)

	fileDownloaded, err := DownloadAvatar(urlQuery.String())

	if err != nil {
		fmt.Println(err.Error())
		ctx.JSON(http.StatusInternalServerError, gin.H{"msg": err.Error()})
		return
	}
	defer fileDownloaded.Close()
	filename, err := StorageHandle.CreateFileIOReaderAutoFolder("file.svg", fileDownloaded)
	if err != nil {
		fmt.Println(err.Error())
		ctx.JSON(http.StatusInternalServerError, gin.H{"msg": err.Error()})
		return
	}
	ctx.JSON(http.StatusOK, gin.H{"avatar": filename})
}

func DownloadAvatar(url string) (io.ReadCloser, error) {

	// Get the data
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	return resp.Body, nil
}
