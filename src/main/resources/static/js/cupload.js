(function(window, document) {

	//定义上传类
	let Cupload = function (options) {

		//初始化 new对象
		if (!(this instanceof Cupload)) {
			return new Cupload(options)
		}

		//设置默认参数
		this.localValue = {
			ele: '#cupload',
			name: 'image',
			num: 1,
			/*input 的 name*/
			names: ['xx', 'ww'],
			width: 148,
			height: 148,
		};

		// 参数覆盖
		this.opt = this.extend(this.localValue, options, true)

		// 参数校验
		if (this.opt.num !== new Set(this.opt.names).size) {
			throw new Error("参数异常");
		}


		// 每个图片元素
		this.imgs = [];


		if ((typeof options.ele) === "string") {
			// 目标dom
			this.opt.ele = document.querySelector(options.ele);
		} else {
			this.opt.ele = options.ele;
		}
		// 初始化 dom
		this.initDom();
	};

	Cupload.prototype = {
		constructor: this,
		/**
		 * 初始化
		 * <cupload>
		 *
		 *     	<!-- 图片列表容器 -->
		 *		<ul class='cupload-image-list' style="..."></ul>
		 *
		 *		<!--上传框-->
		 * 		<div class='cupload-upload-box' style='....'>
		 * 		    <!--上传按钮 -->
		 * 		    <span class=‘cupload-upload-btn’ style='....'>+</span>
		 * 		    <!-- 上传input -->
		 * 		    <input class="cupload-upload-input" style="..."  onchange=' () && uploadImage()'
		 * 		    									type="file" multiple="multiple" accept="image/*"></input>
		 * 		</div>
		 * 		<!--遮罩层-->
		 *		<div class="cupload-overlay" style="..." onclick=‘zoomOutImage()’></div>
		 *
		 * </cupload>
		 */

		initDom: function () {
			// 创建图片列表容器
			this.createImageList();
			// 创建上传框
			this.createUploadBox();
			// 创建遮罩层
			this.createOverlay();
		},

		/**
		 * 参数覆盖
		 * @param o
		 * @param n
		 * @param override
		 * @returns {*}
		 */
		extend: function (o, n, override) {
			for (let key in n) {
				if (n.hasOwnProperty(key) && (!o.hasOwnProperty(key) || override)) {
					o[key] = n[key]
				}
			}
			return o
		},

		/**
		 * 创建图片列表
		 * <ul class='cupload-image-list' style="..."></ul>
		 */
		createImageList: function () {
			this.imageList = document.createElement('ul');
			this.imageList.className = 'cupload-image-list';
			this.imageList.style.margin = 0;
			this.imageList.style.padding = 0;
			this.imageList.style.display = 'inline-block';
			this.imageList.style.minHeight = this.opt.height;
			this.opt.ele.appendChild(this.imageList);
		},

		/**
		 * 创建上传框
		 * <div class='cupload-upload-box' style='....'>
		 */
		createUploadBox: function () {
			this.uploadBox = document.createElement('div');
			this.uploadBox.className = 'cupload-upload-box'
			this.uploadBox.style.position = 'relative'
			this.uploadBox.style.display = 'inline-block'
			this.uploadBox.style.textAlign = 'center'
			this.uploadBox.style.backgroundColor = '#fbfdff'
			this.uploadBox.style.border = '1px dashed #c0ccda'
			this.uploadBox.style.borderRadius = '6px'
			this.uploadBox.style.WebkitBoxSizing = 'border-box'
			this.uploadBox.style.boxSizing = 'border-box'
			this.uploadBox.style.width = this.opt.width + 'px'
			this.uploadBox.style.height = this.opt.height + 'px'
			this.uploadBox.style.lineHeight = this.opt.height + 'px'
			// 插入到目标节点后
			this.opt.ele.appendChild(this.uploadBox);

			// 创建上传按钮
			this.createUploadBtn();

			// 创建上传input
			this.createUploadInput();

			this.uploadBox.onmouseover = () => {
				this.uploadBox.style.borderColor = '#409eff'
			};

			this.uploadBox.onmouseout = () => {
				this.uploadBox.style.borderColor = '#c0ccda'
			};
		},

		/**
		 * 创建遮罩( after this.opt.ele )
		 * <div class="cupload-overlay" style="..."></div>
		 */
		createOverlay: function() {
			this.overlay = document.createElement('div')
			this.overlay.className = 'cupload-overlay'
			this.overlay.style.display = "none"
			this.overlay.style.position = "fixed"
			this.overlay.style.textAlign = "center"
			this.overlay.style.top = 0
			this.overlay.style.right = 0
			this.overlay.style.bottom = 0
			this.overlay.style.left = 0
			this.overlay.style.zIndex = 9115
			this.overlay.style.backgroundColor = "rgba(0,0,0,.3)"
			this.opt.ele.appendChild(this.overlay)
			this.overlay.onclick = () => {
				this.zoomOutImage()
			};
		},

		/**
		 * 创建上传按钮
		 * <span class=‘cupload-upload-btn’ style='....'>+</span>
		 */
		createUploadBtn: function () {
			this.uploadBtn = document.createElement('span')
			this.uploadBtn.className = 'cupload-upload-btn'
			this.uploadBtn.style.position = 'absolute'
			this.uploadBtn.style.left = this.opt.width / 2 - 14 + 'px'
			this.uploadBtn.style.fontSize = '28px'
			this.uploadBtn.style.color = '#8c939d'
			this.uploadBtn.innerHTML = '+'
			// 添加到 上传框 后面
			this.uploadBox.appendChild(this.uploadBtn)
		},

		/**
		 * 创建上传input
		 * <input class="cupload-upload-input" style="..." type="file" multiple="multiple" accept="image/*"></input>
		 */
		createUploadInput: function () {
			this.uploadInput = document.createElement('input')
			this.uploadInput.className = 'cupload-upload-input'
			this.uploadInput.style.position = 'absolute'
			this.uploadInput.style.top = 0
			this.uploadInput.style.right = 0
			this.uploadInput.style.width = '100%'
			this.uploadInput.style.height = '100%'
			this.uploadInput.style.opacity = 0
			this.uploadInput.style.cursor = 'pointer'
			this.uploadInput.type = 'file'
			this.uploadInput.multiple = 'multiple'
			this.uploadInput.accept = 'image/*'
			this.uploadInput.title = ''
			this.uploadBox.appendChild(this.uploadInput)
			this.uploadInput.onchange = () => {
				console.log("this.uploadInput.onchange...")
				this.removeUploadBox()
				this.uploadImage()
			};
		},

		/**
		 * 上传图片
		 * @returns {boolean}
		 */
		uploadImage: function() {
			console.log("上传图片", this.uploadInput, this.uploadInput.files);
			let files = this.uploadInput.files;
			if (files.length + this.imgs.length > this.opt.length) {
				alert('图片数量超出限制，请重新选择');
				this.createUploadBox();
			} else {
				for (let index = 0; index < files.length; index++) {
					let file = files[index];
					if (!file || this.limitedSize(file)) {
						this.createUploadBox()
						return false
					}
					let reader = new FileReader();
					reader.filename = file.name;
					reader.onload = (e) => {
						// 图片像素校验
						this.limitedWidthAndHeight(e.target.result, e.target.filename)
					};
					reader.readAsDataURL(file);
				}
			}
		},

		//检测图片大小限制
		limitedSize: function(file) {
			if (this.opt.minSize && file.size < this.opt.minSize * 1024) {
				alert('图片' + file.name + '大小未到最小限制，请重新选择')
				return true
			}
			if (this.opt.maxSize && file.size > this.opt.maxSize * 1024) {
				alert('图片' + file.name + '大小超出最大限制，请重新选择')
				return true
			}
			if (this.opt.limitedSize && file.size > this.opt.limitedSize * 1024) {
				alert('图片' + file.name + '大小不符合要求，请重新选择')
				return true
			}
			return false
		},

		/**
		 * 检测图片像素限制
		 * @param src	base64 图片
		 * @param name	图片名称
		 */
		limitedWidthAndHeight: function(src, name) {
			// 检测图片像素限制...
			this.foreachNum(src, name, this.width, this.height)
		},

		/**
		 * 上传图片
		 * @param src	base64 data
		 * @param name	filename
		 * @param width
		 * @param height
		 */
		foreachNum: function(src, name, width, height) {
			if(this.opt.url) {
				// key 默认为 image
				let key = this.opt.name
				let data = {}
				data[key] = src
				data['filename'] = name
				this.ajaxUploadImage(data, uri => {
					this.createImageBox(uri, name, width, height)
				});
			} else {
				this.createImageBox(src, name, width, height)
			}
		},

		/**
		 * 图片上传
		 * @param data
		 * @param success
		 */
		ajaxUploadImage: function(data,success) {
			// 服务器地址
			let url = this.opt.url;
			let fileName = data.filename;
			if (url) {
				console.log("success");
				success("https://suse-mall.oss-cn-chengdu.aliyuncs.com/2022/03/a0b07c8a33424400a45e5abbb2038c82.png");
				this.imgs.push({
					fileName: fileName,
					url: 'https://suse-mall.oss-cn-chengdu.aliyuncs.com/2022/03/a0b07c8a33424400a45e5abbb2038c82.png'
				});
			}
		},

		/**
		 * 创建图片框(展示图片)
		 * @param src	图片uri
		 * @param name	图片名称
		 * @param width
		 * @param height
		 * @param state
		 *
		 * <li class="cupload-image-box" style="..."></li>
		 */
		createImageBox: function (src, name, width, height, state = true) {
			console.log("创建图片展示框")
			let imgBoxItem = document.createElement('li')
			imgBoxItem.className = 'cupload-image-box'
			imgBoxItem.style.position = 'relative'
			imgBoxItem.style.display = 'inline-block'
			imgBoxItem.style.marginRight = 8 + 'px'
			imgBoxItem.style.backgroundColor = '#fbfdff'
			imgBoxItem.style.border = '1px solid #c0ccda'
			imgBoxItem.style.borderRadius = '6px'
			imgBoxItem.style.WebkitBoxSizing = 'border-box'
			imgBoxItem.style.boxSizing = 'border-box'
			imgBoxItem.style.width = this.opt.width + 'px'
			imgBoxItem.style.height = this.opt.height + 'px'

			this.imageList.appendChild(imgBoxItem)

			// 创建图片预览框
			// this.createImagePreview(src, width, height)

			// 创建图片input
			// this.createImageInput(src)

			// 创建删除
			// this.createImageDelete(name)


			// let _this = this
			// for (let m = 0; m <= this.i; m++) {
			// 	this.imageBox[m].index = m
			//
			// 	this.imageBox[m].onmouseover = function (n) {
			// 		return function () {
			// 			_this.showImageDelete(n)
			// 		}
			// 	}(m)
			//
			// 	this.imageBox[m].onmouseout = function (n) {
			// 		return function () {
			// 			_this.hideImageDelete(n)
			// 		}
			// 	}(m)
			//
			// }
			// this.i++
		},

		//创建图片预览框
		createImagePreview: function(src, width, height) {
			console.log("创建图片预览框");
			this.imagePreview[this.i] = document.createElement('img')
			this.imagePreview[this.i].className = 'cupload-image-preview'
			this.imagePreview[this.i].style.position = 'absolute'
			this.imagePreview[this.i].style.top = 0
			this.imagePreview[this.i].style.left = 0
			this.imagePreview[this.i].style.right = 0
			this.imagePreview[this.i].style.bottom = 0
			this.imagePreview[this.i].style.margin = 'auto'
			this.imagePreview[this.i].src = src
			this.setImageAttribute(width, height)
			this.imageBox[this.i].appendChild(this.imagePreview[this.i])
		},

		//创建图片input
		createImageInput: function(src) {
			console.log("创建图片input")
			this.imageInput[this.i] = document.createElement('input')
			this.imageInput[this.i].type = 'hidden'
			this.imageInput[this.i].name = this.opt.name + '[]'
			this.imageInput[this.i].value = src
			this.imageBox[this.i].appendChild(this.imageInput[this.i]);
			this.opt.urls.push(src);
		},

		getUrls: () => {
			return this.opt.urls;
		},

		//创建删除
		createImageDelete: function(name) {
			console.log("创建删除")
			this.imageDelete[this.i] = document.createElement('div')
			this.imageDelete[this.i].className = 'cupload-image-delete'
			this.imageDelete[this.i].style.position = 'absolute'
			this.imageDelete[this.i].style.width = '100%'
			this.imageDelete[this.i].style.height = '100%'
			this.imageDelete[this.i].style.left = 0
			this.imageDelete[this.i].style.top = 0
			this.imageDelete[this.i].style.textAlign = 'center'
			this.imageDelete[this.i].style.color = '#fff'
			this.imageDelete[this.i].style.opacity = 0
			this.imageDelete[this.i].style.cursor = 'zoom-in'
			this.imageDelete[this.i].style.backgroundColor = 'rgba(0,0,0,.5)'
			this.imageDelete[this.i].style.WebkitTransition = '.3s'
			this.imageDelete[this.i].style.transition = '.3s'
			this.imageDelete[this.i].title = name
			this.imageBox[this.i].appendChild(this.imageDelete[this.i])
			this.createDeleteBtn()
			this.createSortBtn()
			let _this = this
			for (let m = 0; m <= this.i; m++) {
				this.imageDelete[m].onclick = function(n) {
					return function() {
						_this.zoomInImage(n)
					}
				}(m)
			}
		},

		//创建删除按钮
		createDeleteBtn: function() {
			this.deleteBtn[this.i] = document.createElement('span')
			this.deleteBtn[this.i].className = 'cupload-delete-btn'
			this.deleteBtn[this.i].style.position = 'absolute'
			this.deleteBtn[this.i].style.top = 0
			this.deleteBtn[this.i].style.right = 0
			this.deleteBtn[this.i].style.margin = 0
			this.deleteBtn[this.i].style.padding = 0
			this.deleteBtn[this.i].style.fontSize = '18px'
			this.deleteBtn[this.i].style.width = '24px'
			this.deleteBtn[this.i].style.height = '24px'
			this.deleteBtn[this.i].style.cursor = 'pointer'
			this.deleteBtn[this.i].style.backgroundImage = "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABmJLR0QA/wD/AP+gvaeTAAAAwUlEQVRYhe2WwQ3CMAxFPxXqOjAAqGwHo3QNCIxSxuBzcaRStcRxcin4XSJHjv2aNkoBRwHJhmSgnhvJpqbAPqN5ZKeprbU8ydhvEgDoJ2uqCHQyXhW5Maf7mrUEyYdhu7WEab+5HXiZzJXPp88Uijsm6tQ7KkZcYF0CckSDNi5i7uudzqXipbkx63oFLuACPymwzcy/4/NKTcV2/Dp2AQBPACB5sBYneRzXyl18qfAXHDlbBFqRGAoaD1KjzRb4G96BvtfyCUSIygAAAABJRU5ErkJggg==')"
			this.deleteBtn[this.i].style.backgroundSize = '18px 18px'
			this.deleteBtn[this.i].style.backgroundRepeat = 'no-repeat'
			this.deleteBtn[this.i].style.backgroundPosition = 'right top'
			this.deleteBtn[this.i].innerHTML = ''
			this.deleteBtn[this.i].title = '删除'
			this.imageDelete[this.i].appendChild(this.deleteBtn[this.i])
			let _this = this

			for (let m = 0; m < this.deleteBtn.length; m++) {
				this.deleteBtn[m].onclick = function (n,self) {
					return () => {
						_this.deleteImage(n)
					};
				}(m, this);
			}

		},

		createSortBtn: function() {
			console.log("createSortBtn");
			this.sortLeftBtn[this.i] = document.createElement('span')
			this.sortLeftBtn[this.i].className = 'cupload-sort-left'
			this.sortLeftBtn[this.i].style.position = 'absolute'
			this.sortLeftBtn[this.i].style.bottom = 0
			this.sortLeftBtn[this.i].style.left = 0
			this.sortLeftBtn[this.i].style.margin = 0
			this.sortLeftBtn[this.i].style.padding = 0
			this.sortLeftBtn[this.i].style.fontSize = '18px'
			this.sortLeftBtn[this.i].style.width = '24px'
			this.sortLeftBtn[this.i].style.height = '24px'
			this.sortLeftBtn[this.i].style.cursor = 'pointer'
			this.sortLeftBtn[this.i].style.backgroundImage = "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABmJLR0QA/wD/AP+gvaeTAAABP0lEQVRYhe2UIUsEURRGv7suGza4sCBoENYgGMwWs/4A/4AWwWRyMfgHRCyKGhWsYtegRYOCCGoUk1kwKLiws8dywRFGcXZ23yDMiW8e75z3YK5UUPBfAQbzlK8BEbCa9axySrFJ2pC0klWcGsCALb7Y8aAg8gHgICZfDyJ2eQU4dnEHCPf8QBU4dXkELIaU14BLl7eB+ZDyOnDt8g9gLqR8BHhw+Rsw00/ft98IaEg6lzTmS2eSbnroiyTtm9lT4ldgm/6z+9sLTPoLDPnSraTk2u5oSdo0s7sfdwATwLPXvgBTPQz4G0ADePSIV2A6j4hh4N4j3oHZPCLqwJVHtAg5D2IRNeDCI8JOxFhEFTjxiA6wnEdEBTiKRTTziCgDh7GhspT1zFKazWbWlrQgac+XRrMGdA0wDqS6QEFBEp/yS6NBq8E1tgAAAABJRU5ErkJggg==')"
			this.sortLeftBtn[this.i].style.backgroundSize = '18px 18px'
			this.sortLeftBtn[this.i].style.backgroundRepeat = 'no-repeat'
			this.sortLeftBtn[this.i].style.backgroundPosition = 'left bottom'
			this.sortLeftBtn[this.i].innerHTML = ''
			this.sortLeftBtn[this.i].title = '左移'
			this.imageDelete[this.i].appendChild(this.sortLeftBtn[this.i])

			this.sortRightBtn[this.i] = document.createElement('span')
			this.sortRightBtn[this.i].className = 'cupload-sort-right'
			this.sortRightBtn[this.i].style.position = 'absolute'
			this.sortRightBtn[this.i].style.bottom = 0
			this.sortRightBtn[this.i].style.right = 0
			this.sortRightBtn[this.i].style.margin = 0
			this.sortRightBtn[this.i].style.padding = 0
			this.sortRightBtn[this.i].style.fontSize = '18px'
			this.sortRightBtn[this.i].style.width = '24px'
			this.sortRightBtn[this.i].style.height = '24px'
			this.sortRightBtn[this.i].style.cursor = 'pointer'
			this.sortRightBtn[this.i].style.backgroundImage = "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABmJLR0QA/wD/AP+gvaeTAAAA4UlEQVRYhe3WMUpDQRCA4XkKWlhIqnQWwc4iKbyBTVohJ/AC2lh4gxxBA7lAqhwgvbWtpYWVnVgI4mfhghKeEPCFSeD93cIu8w+7OzMRLS3bDga4Qy9LYOqbZ5xkCBzjqUi84DRD4giPReIVZxkSXTwUiTcMMyQ6uC8S7xhlSBxgUSQ+cJEhsY95kfjEVYbEHmZ+GNftq2oOHkbETkMeuxExiYjzsh5XVXXz525cWz+Xv2MuZ6qhzFdms66gSVZ9hOsKnvcNZRYimaVYZjOS2Y5lDyQ2YCTr41bWUNrS8l++ABQQn/PCTE8cAAAAAElFTkSuQmCC')"
			this.sortRightBtn[this.i].style.backgroundSize = '18px 18px'
			this.sortRightBtn[this.i].style.backgroundRepeat = 'no-repeat'
			this.sortRightBtn[this.i].style.backgroundPosition = 'right bottom'
			this.sortRightBtn[this.i].innerHTML = ''
			this.sortRightBtn[this.i].title = '右移'
			this.imageDelete[this.i].appendChild(this.sortRightBtn[this.i])
			let _this = this
			for (let m = 0; m <= this.i; m++) {
				this.sortLeftBtn[m].onclick = function(n) {
					return function() {
						_this.sortLeft(event, n)
					}
				}(m)

				this.sortRightBtn[m].onclick = function(n) {
					return function() {
						_this.sortRight(event, n)
					}
				}(m)
			}
		},

		//设置默认图片
		setDefaultImage: function() {
			this.imageBox[this.i].style.backgroundColor = "#B2B2B2"
			this.imageDelete[this.i].title = '图片不存在'
			this.imagePreview[this.i].src = 'data:image/png;base64,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'
			if (130 / this.opt.width > 105 / this.opt.height) {
				this.imagePreview[this.i].style.width = this.opt.width - 2 + 'px'
				this.imagePreview[this.i].style.height = 105 / (130 / this.opt.width) - 2 + 'px'
			} else {
				this.imagePreview[this.i].style.width = 130 / (105 / this.opt.height) - 2 + 'px'
				this.imagePreview[this.i].style.height = this.opt.height - 2 + 'px'
			}
		},

		//设置图片宽高
		setImageAttribute: function(width, height) {
			if (width / this.opt.width > height / this.opt.height) {
				this.imagePreview[this.i].style.width = this.opt.width - 2 + 'px'
				this.imagePreview[this.i].style.height = height / (width / this.opt.width) - 2 + 'px'
			} else {
				this.imagePreview[this.i].style.width = width / (height / this.opt.height) - 2 + 'px'
				this.imagePreview[this.i].style.height = this.opt.height - 2 + 'px'
			}
		},

		//data图片预览
		showImagePreview: function() {
			console.log("data图片预览")
			let obj = this.opt.data
			if (obj.length >= this.opt.num) {
				this.removeUploadBox()
			}
			let _this = this
			let tempImage = new Image()
			tempImage.src = obj[this.i]
			tempImage.onload = function() {
				_this.createImageBox(obj[_this.i], obj[_this.i], this.width, this.height)
				setTimeout(function() {
					if (obj[_this.i]) {
						_this.showImagePreview()
					}
				}, 0);
			}
			tempImage.onerror = function() {
				_this.createImageBox(obj[_this.i], obj[_this.i], 0, 0, false)
				setTimeout(function() {
					if (obj[_this.i]) {
						_this.showImagePreview()
					}
				}, 0);
			}
		},

		//图片放大预览
		zoomInImage: function(n) {
			if(event.target.classList[0] === 'cupload-delete-btn' || event.target.classList[0] === 'cupload-sort-right' || event.target.classList[0] === 'cupload-sort-left') {
				return;
			}
			if(this.imagePreview[n].src == 'data:image/png;base64,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') {
				alert('图片不存在')
				return;
			}
			this.zommImage = document.createElement('img')
			this.zommImage.style.display = "inline-block"
			this.zommImage.style.verticalAlign = "middle"
			this.zommImage.src = this.imageArr[n]
			if (this.widthArr[n] / window.innerWidth > this.heightArr[n] / window.innerHeight) {
				this.zommImage.style.width = 0.8 * window.innerWidth + 'px'
				this.zommImage.style.height = 0.8 * this.heightArr[n] / (this.widthArr[n] / window.innerWidth) + 'px'
			} else {
				this.zommImage.style.width = 0.8 * this.widthArr[n] / (this.heightArr[n] / window.innerHeight) + 'px'
				this.zommImage.style.height = 0.8 * window.innerHeight + 'px'
			}
			this.overlay.appendChild(this.zommImage)
			this.overlay.style.lineHeight = window.innerHeight + 'px'
			this.overlay.style.cursor = "zoom-out"
			this.overlay.style.display = "block"
		},

		//关闭图片放大预览
		zoomOutImage: function() {
			this.overlay.style.display = "none"
			this.zommImage.remove()
		},

		//检测当前图片数量，判断是否创建上传框
		isCreateUploadBox: function() {
			this.removeUploadBox()
			if (this.imageList.children.length < this.opt.num) {
				// 创建上传框
				this.createUploadBox();
			}
		},

		//删除图片
		deleteImage: function(n) {
			console.log("删除图片", n);
			console.log("this.imageBox[n]", this.imageBox[n]);
			/* 删除图片框 - remove dom节点*/
			this.imageBox[n].remove()

			this.imageArr[n] = null;
			this.removeUploadBox()
			if (this.imageList.children.length < this.opt.num) {
				this.createUploadBox()
			}
		},

		sortLeft: function(event, n) {
			if(this.imageBox[n].previousSibling) {
				this.imageList.insertBefore(this.imageBox[n], this.imageBox[n].previousSibling)
			}
		},

		sortRight: function(event, n) {
			if(this.imageBox[n].nextSibling) {
				this.imageList.insertBefore(this.imageBox[n].nextSibling, this.imageBox[n])
			}
		},

		/**
		 * 移除上传框
		 */
		removeUploadBox: function() {
			this.uploadBox.remove()
		},

		//显示图片删除
		showImageDelete: function(m) {
			this.imageDelete[m].style.opacity = 1
		},

		//隐藏图片删除
		hideImageDelete: function(m) {
			this.imageDelete[m].style.opacity = 0
		},
	}

	window.Cupload = Cupload;
})(window, document)
