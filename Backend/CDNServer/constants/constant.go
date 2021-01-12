package constants

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
