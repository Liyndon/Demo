/**
 * 计算textarae字符长度
 */
(function($) {
	$.fn.textareaCount = function(options, value, fn) {
		if (typeof options == "string") {
			return $.fn.textareaCount.methods[options].call(this, value);
		};
		var defaults = {
			maxCharacterSize : 100,
			defaultText : '请输入……',
			fit:true,
			width:300,
			originalStyle : 'originalTextareaInfo',
			warningStyle : 'warningTextareaInfo',
			warningNumber : 20,
			displayFormat : '#input characters | #words words',
			showInputInfo : true
		};
		var options = $.extend(defaults, options);
		var container = $(this);
		container.css('color', '#ccc');
		container.val(options.defaultText);
		var state = $.data(this, 'textareaCount');
		if (state) {
			$.extend(state.options, options);
		} else {
			state = $.data(this, 'textareaCount', {
						options : options
					});
		}
		$("<input type='hidden' value=" + options.defaultText + ">")
				.insertBefore(container);// 存储默认值
		$("<div class='charleft'>&nbsp;</div>").insertAfter(container);
		var charLeftInfo = getNextCharLeftInformation(container);
		if(options.fit){
			setTimeout(function(){
				container.css('width', '100%');
			},500);
			$(window).resize(function(){
				setTimeout(function(){
					charLeftInfo.width(container.width());
				},200);
			});
		}else{
			container.css('width', options.width+'px');
		}
		if (!options.showInputInfo) {// 不显示输入限制提示信息
			charLeftInfo.css({
						'display' : 'none'
					});
		}
		charLeftInfo.addClass(options.originalStyle);
		setTimeout(function(){
			var charLeftCss = {
				'color' : '#777',
				'padding-top' : '3px',
				'text-align' : 'right',
				'height' : '23px',
				'width' : container.width()
			};
			charLeftInfo.css(charLeftCss);
		},600);

		var numInput = 0;
		var maxCharacters = options.maxCharacterSize;
		var numLeft = 0;
		var numWords = 0;

		container.bind('keyup', function(event) {
					limitTextAreaByCharacterCount('keyup');
				}).bind('mouseover', function(event) {
					setTimeout(function() {
								limitTextAreaByCharacterCount('mouseover');
							}, 1);
				}).bind('focus', function(event) {
					setTimeout(function() {
								limitTextAreaByCharacterCount('focus');
							}, 1);
				}).bind('focusout', function(event) {
					setTimeout(function() {
								limitTextAreaByCharacterCount('focusout');
							}, 1);
				}).bind('focusin', function(event) {
					setTimeout(function() {
								limitTextAreaByCharacterCount('focusin');
							}, 1);
				}).bind('paste', function(event) {
					setTimeout(function() {
								limitTextAreaByCharacterCount('paste');
							}, 1);
				});

		function limitTextAreaByCharacterCount(dealType) {
			charLeftInfo.html(countByCharacters(dealType));
			if (typeof fn != 'undefined') {
				fn.call(this, getInfo());
			}
			return true;
		}

		function countByCharacters(dealType) {
			var content = container.val();
			var contentLength = content.length;

			if (options.maxCharacterSize > 0) {
				if (dealType == 'focusout' && contentLength == 0) {
					container.val(options.defaultText);
					container.css('color', '#ccc');
				}
				if (dealType == 'focus' && content == options.defaultText) {
					container.val("");
					container.css('color', '#000');
				}

				if (contentLength >= options.maxCharacterSize) {
					content = content.substring(0, options.maxCharacterSize);
				}

				var newlineCount = getNewlineCount(content);

				var systemmaxCharacterSize = options.maxCharacterSize
						- newlineCount;
				if (!isWin()) {
					systemmaxCharacterSize = options.maxCharacterSize
				}
				if (contentLength > systemmaxCharacterSize) {
					var originalScrollTopPosition = this.scrollTop;
					container.val(content.substring(0, systemmaxCharacterSize));
					this.scrollTop = originalScrollTopPosition;
				}
				charLeftInfo.removeClass(options.warningStyle);
				if (systemmaxCharacterSize - contentLength <= options.warningNumber) {
					charLeftInfo.addClass(options.warningStyle);
				}

				if (container.val() != options.defaultText) {
					numInput = container.val().length + newlineCount;
				} else {
					numInput = 0;
				}
				if (!isWin()) {
					numInput = container.val().length;
				}

				numWords = countWord(getCleanedWordString(container.val()));

				numLeft = maxCharacters - numInput;
			} else {
				var newlineCount = getNewlineCount(content);
				if (container.val() != options.defaultText) {
					numInput = container.val().length + newlineCount;
				} else {
					numInput = 0;
				}

				if (!isWin()) {
					numInput = container.val().length;
				}
				numWords = countWord(getCleanedWordString(container.val()));
			}

			return formatDisplayInfo();
		}

		function formatDisplayInfo() {
			var format = options.displayFormat;
			format = format.replace('#input', numInput);
			format = format.replace('#words', numWords);
			if (maxCharacters > 0) {
				format = format.replace('#max', maxCharacters);
				format = format.replace('#left', numLeft);
			}
			return format;
		}

		function getInfo() {
			var info = {
				input : numInput,
				max : maxCharacters,
				left : numLeft,
				words : numWords
			};
			return info;
		}

		function getNextCharLeftInformation(container) {
			return container.next('.charleft');
		}

		function isWin() {
			var strOS = navigator.appVersion;
			if (strOS.toLowerCase().indexOf('win') != -1) {
				return true;
			}
			return false;
		}

		function getNewlineCount(content) {
			var newlineCount = 0;
			for (var i = 0; i < content.length; i++) {
				if (content.charAt(i) == '\n') {
					newlineCount++;
				}
			}
			return newlineCount;
		}

		function getCleanedWordString(content) {
			var fullStr = content + " ";
			var initial_whitespace_rExp = /^[^A-Za-z0-9]+/gi;
			var left_trimmedStr = fullStr.replace(initial_whitespace_rExp, "");
			var non_alphanumerics_rExp = rExp = /[^A-Za-z0-9]+/gi;
			var cleanedStr = left_trimmedStr.replace(non_alphanumerics_rExp,
					" ");
			var splitString = cleanedStr.split(" ");
			return splitString;
		}

		function countWord(cleanedWordString) {
			var word_count = cleanedWordString.length - 1;
			return word_count;
		}
	};
	function getValue() {
		var params = $(this).prev('input[type="hidden"]').val();
		if (this.val() == params) {
			return "";
		}
		return this.val();
	};
	function setValue(value) {
		this.val(value);
		this.css('color', '#000');
	};
	$.fn.textareaCount.methods = {
		getValue : function() {
			return getValue.call(this);
		},
		setValue : function(value) {
			return setValue.call(this, value);
		}
	};
})(jQuery);