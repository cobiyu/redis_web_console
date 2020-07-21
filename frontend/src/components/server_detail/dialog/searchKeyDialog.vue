<template>
  <v-row justify="center">
    <v-dialog v-model="visible" persistent width="80%">
      <v-card>
        <v-card-title>
          {{selectSearchDb}} DB Search Key
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-form
              ref="searchKeyForm"
              v-model="valid"
            >
              <v-row>
                <v-col>
                  <v-text-field
                    v-model="key"
                    outlined
                    label="Key"
                    :rules="[rules.required]"
                    auto-grow
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="searchKey">Search</v-btn>
          <v-btn color="blue darken-1" text @click="closeDialog">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: 'searchKeyDialog',
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    selectSearchDb: {
      type: String
    }
  },
  data () {
    return {
      searchWord: null,
      valid: null,
      key: null,
      rules: {
        required: value => !!value || 'Required.'
      }
    }
  },
  watch: {
    visible () {
      this.key = null
      if (this.$refs.searchKeyForm) {
        this.$refs.searchKeyForm.resetValidation()
      }
    }
  },
  methods: {
    searchKey () {
      if (this.$refs.searchKeyForm.validate()) {
        this.$emit('search-key', {
          db: this.selectSearchDb,
          key: this.key
        })

        this.closeDialog()
      }
    },
    closeDialog () {
      this.$emit('close-search-key-dialog')
    }
  }
}
</script>

<style scoped>

</style>
