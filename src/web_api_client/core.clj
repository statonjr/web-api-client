(ns web-api-client.core
  (:require [org.httpkit.client :as http]
            [hickory.core :as hickory]
            [hickory.select :as s]))

(defn fetch
  "Fetch a URL and returns a map of a Hickory-parsed document"
  [url]
  (let [doc (http/get url)]
    (hickory/as-hickory
     (hickory/parse (:body @doc)))))

(defn items
  "Takes a DOCument and returns a map of nodes that have an :itemtype attribute"
  [doc]
  (s/select (s/attr :itemtype) doc))

(defn print-items
  "Print the list of ITEMS"
  [items]
  (map
   #(get-in % [:attrs :itemtype])
   items))

(defn properties
  "Takes a DOCument and returns a map of nodes that have an :itemprop attribute"
  [doc]
  (s/select (s/attr :itemprop) doc))

(defn print-properties
  "Print the list of PROPerties"
  [props]
  (map
   #(get-in % [:attrs :itemprop])
   props))

(defn links
  "Takes a DOCument and returns a map of nodes that have an :href attribute"
  [doc]
  (s/select (s/attr :href) doc))

(defn print-links
  "Print the list of LINKS"
  [links]
  (map
   #(get-in % [:attrs :href])
   links))

(defn item-attr
  "Takes an ATTribute and a DOCument. Returns the content of the ATTribute of the item in the DOCument"
  [att doc]
  (-> (s/select (s/attr :itemprop #(.startsWith % att))
                doc)
      first :content first))

(defn actions
  "Takes a DOCument. Returns a list of actions with which a client can interact."
  [doc]
  (map
   #(get-in % [:attrs :data-rel])
   (s/select (s/attr :data-rel) doc)))
