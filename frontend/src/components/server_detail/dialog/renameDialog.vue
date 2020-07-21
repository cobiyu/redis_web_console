<template>
  <v-row justify="center">
    <v-dialog
      v-model="visible"
      persistent
      max-width="600px"
      @input="v => v || init()"
    >
      <v-card>
        <v-form
          ref="renameForm"
          v-model="valid"
        >
          <v-card-title>
            RENAME
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model="selectKey"
                    label="Key"
                    :readonly="true"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    :rules="[rules.required, rules.duplicate]"
                    v-model="newKey"
                    label="New Key"
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="rename">Rename</v-btn>
            <v-btn color="blue darken-1" text @click="closeDialog">Close</v-btn>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: 'renameDialog',
  data () {
    return {
      newKey: null,
      valid: true,
      rules: {
        required: value => !!value || 'Required.',
        duplicate: value => this.selectKey !== value || 'Duplicate'
      }
    }
  },
  computed: {
    isModified () {
      return this.selectKey !== this.newKey
    }
  },
  mounted () {
    this.newKey = null
  },
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    selectKey: {
      type: String
    }
  },
  watch: {
    visible (isVisible) {
      if (isVisible) {
        this.newKey = null
        if (this.$refs.renameForm) {
          this.$refs.renameForm.resetValidation()
        }
      }
    }
  },
  methods: {
    rename () {
      if (this.$refs.renameForm.validate()) {
        this.$emit('rename', this.newKey)
      }
    },
    closeDialog () {
      this.$emit('close-dialog')
    }
  }
}
</script>

<style scoped>

</style>
